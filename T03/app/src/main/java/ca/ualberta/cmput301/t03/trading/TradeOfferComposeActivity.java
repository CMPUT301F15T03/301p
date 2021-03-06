/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.ualberta.cmput301.t03.trading;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.exceptions.ExceptionUtils;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;

import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.inventory.ItemsAdapter;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeModificationException;
import ca.ualberta.cmput301.t03.user.User;

/**
 * The view portion of the TradeOfferCompose triplet. this is the view a user will see when
 * they wish to compose a trade with another user and their item.
 */
public class TradeOfferComposeActivity extends AppCompatActivity implements Observer{

    private final Activity activity = this;
    public final static String EXTRA_PREV_TRADE_UUID = "prevuuid";

    private Trade model;
    private TradeOfferComposeController controller;

    private TextView ownerUsername;
    private Button offerButton;
    private Button cancelButton;
    private Button addItemButton;

    private ListView ownerItemListView;
    private ItemsAdapter<Inventory> ownerItemAdapter;
    private ListView borrowerItemListView;
    private ItemsAdapter<Inventory> borrowerItemAdapter;

    private Inventory borrowerInventory;
    private Inventory ownerInventory;

    private Inventory ownerItems;
    private Inventory borrowerItems;
    private Button requestItemButton;

    /**
     * Sets up the view with all components, the model, and the controller
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_offer_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra(EXTRA_PREV_TRADE_UUID)) {
            UUID previousUUID = (UUID) getIntent().getSerializableExtra(EXTRA_PREV_TRADE_UUID);

        }

        ownerUsername = (TextView) findViewById(R.id.tradeComposeOtherUser);
        offerButton = (Button) findViewById(R.id.tradeComposeOffer);
        cancelButton = (Button) findViewById(R.id.tradeComposeCancel);
        addItemButton = (Button) findViewById(R.id.tradeComposeAddItem);
        requestItemButton = (Button) findViewById(R.id.tradeComposeRequestItem);

        AdapterView.OnItemClickListener deleteItemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) parent.getItemAtPosition(position);
                borrowerItems.removeItem(item);
            }
        };

        ownerItemListView = (ListView) findViewById(R.id.tradeComposeOwnerItem);
        ownerItemListView.setOnItemClickListener(deleteItemListener);

        borrowerItemListView = (ListView) findViewById(R.id.tradeComposeBorrowerItems);
        borrowerItemListView.setOnItemClickListener(deleteItemListener);


        AsyncTask worker = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {

                    if (getIntent().hasExtra(EXTRA_PREV_TRADE_UUID)) {
                        UUID prevUUID = (UUID) getIntent().getSerializableExtra(EXTRA_PREV_TRADE_UUID);
                        Trade prevTrade = new Trade(prevUUID, TradeApp.getContext());

                        model = new Trade(prevTrade.getOwner(),
                                prevTrade.getBorrower(),
                                prevTrade.getOwnersItems(),
                                prevTrade.getBorrowersItems(),
                                TradeApp.getContext());
                    } else {

                        model = new Trade(Parcels.<User>unwrap(getIntent().getParcelableExtra("trade/compose/borrower")),
                                Parcels.<User>unwrap(getIntent().getParcelableExtra("trade/compose/owner")),
                                new Inventory(),
                                new Inventory() {{
                                    addItem(Parcels.<Item>unwrap(getIntent().getParcelableExtra("trade/compose/item")));
                                }},
                                TradeApp.getContext());
                    }

                    model.getBorrower().getTradeList().addTrade(model);
                    model.addObserver(model.getBorrower());
                    model.getOwner().getTradeList().addTrade(model);
                    model.addObserver(model.getOwner());
                } catch (IOException e) {
                    throw new RuntimeException("Primary User failed to get TradeList");
                } catch (ServiceNotAvailableException e) {
                    ExceptionUtils.toastLong("Trade failed to create: app is offline");
                    activity.finish();
                }

                controller = new TradeOfferComposeController(TradeApp.getContext(), model);

                ownerItems = model.getOwnersItems();
                ownerItems.addObserver(TradeOfferComposeActivity.this);

                try {
                    borrowerItems = model.getBorrowersItems();
                    borrowerItems.addObserver(TradeOfferComposeActivity.this);
                } catch (ServiceNotAvailableException e) {
                    ExceptionUtils.toastLong("Failed to get borrowers items: app is offline");
                    activity.finish();
                }

                try {
                    borrowerInventory = model.getBorrower().getInventory();
                } catch (IOException e) {
                    borrowerInventory = new Inventory();
                } catch (ServiceNotAvailableException e) {
                    borrowerInventory = new Inventory();
                }
                try {
                    ownerInventory = model.getOwner().getInventory();
                } catch (IOException e) {
                    ownerInventory = new Inventory();
                    Log.e("Error","Failed to fetch owner inventory: " + e.getMessage());
                } catch (ServiceNotAvailableException e) {
                    ownerInventory = new Inventory();
                    Log.e("Error", "Failed to fetch owner inventory: " + e.getMessage());
                }

                return Boolean.FALSE;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                afterFieldsInitialized();
            }

        };

        worker.execute();

    }

    /**
     * Call me after the models have been fetched asynchronously.
     *
     * Binds the models to their view adapters and
     * sets up the rest of the ui widgets to perform
     * their actions.
     *
     */
    private void afterFieldsInitialized() {
        ownerItemAdapter = new ItemsAdapter<Inventory>(TradeApp.getContext(), ownerItems);
        ownerItemListView.setAdapter(ownerItemAdapter);

        borrowerItemAdapter = new ItemsAdapter<Inventory>(TradeApp.getContext(), borrowerItems);
        borrowerItemListView.setAdapter(borrowerItemAdapter);

        requestItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddTradeItemDialog(ownerInventory, ownerItems).show();
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddTradeItemDialog(borrowerInventory, borrowerItems).show();
            }
        });

        ownerUsername.setText(model.getOwner().getUsername());
        offerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask worker = new AsyncTask() {
                    Boolean appIsOffline = Boolean.FALSE;

                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            controller.offerTrade();
                        } catch (ServiceNotAvailableException e) {
                            appIsOffline = Boolean.TRUE;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        ExceptionUtils.toastShort("Trade offered!");
                        if (appIsOffline) {
                            ExceptionUtils.toastLong("Failed to offer trade: app is offline");
                        }

                    }
                };
                worker.execute();
                activity.finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask worker = new AsyncTask() {
                    private Boolean appIsOffline = Boolean.FALSE;

                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            controller.cancelTrade();
                        } catch (ServiceNotAvailableException e) {
                            appIsOffline = Boolean.TRUE;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        ExceptionUtils.toastShort("Trade canceled");
                        if (appIsOffline) {
                            ExceptionUtils.toastLong("Failed to cancel trade: app is offline");
                        }

                    }
                };
                worker.execute();
                activity.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ownerItems != null){
            ownerItems.removeObserver(this);
        }
        if (borrowerItems != null){
            borrowerItems.removeObserver(this);
        }
    }
    /**
     * {@inheritDoc}
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trade_offer_compose, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Factory method to build an alert dialog, used for adding inventory items to trades.
     *
     * browseModel and tradeItemsModel should belong to the same party (owner OR borrower)
     *
     * @param browseModel the Inventory that contains one party's full inventory
     * @param tradeItemsModel the Inventory that contains one party's offered or requested
     *                        trade items.
     * @return an AlertDialog
     */
    private AlertDialog createAddTradeItemDialog(Inventory browseModel, final Inventory tradeItemsModel) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogContent = View.inflate(this, R.layout.trade_item_picker_list, null);

        ListView itemsListView = (ListView) dialogContent.findViewById(R.id.tradeItemListView);

        ItemsAdapter<Inventory> inv = new ItemsAdapter<>(getApplicationContext(), browseModel);
        itemsListView.setAdapter(inv);

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item selected = (Item) parent.getItemAtPosition(position);
                tradeItemsModel.addItem(selected);
            }
        });

        builder.setView(dialogContent); //todo this ui is kind of gross
        builder.setTitle("Add Trade Item");
        builder.setCancelable(false);
        builder.setNegativeButton("Done", null);
        builder.setView(dialogContent);

        AlertDialog d = builder.create();
        return d;
    }

    @Override
    public void update(Observable observable) {

        AsyncTask setBorrowersItems = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    model.setBorrowersItems(borrowerItems);
                } catch (IllegalTradeModificationException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        setBorrowersItems.execute();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                borrowerItemAdapter.notifyDataSetChanged();
                ownerItemAdapter.notifyDataSetChanged();
            }
        });

    }
}
