/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of {ApplicationName}
 *
 * {ApplicationName} is free software: you can redistribute it and/or modify
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
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.TileBuilder;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.filters.item_criteria.StringQueryFilterCriteria;

import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.user.User;

/**
 * The view portion of the TradeOfferCompose triplet. this is the view a user will see when
 * they wish to compose a trade with another user and their item.
 */
public class TradeOfferComposeActivity extends AppCompatActivity {

    private final Activity activity = this;

    private Trade model;
    private TradeOfferComposeController controller;

    private TextView ownerUsername;
    private Button offerButton;
    private Button cancelButton;
    private Button addItemButton;

    private ListView ownerItemListView;
//    private EnhancedSimpleAdapter ownerItemAdapter;
    private List<HashMap<String, Object>> ownerItemTiles;
    private HashMap<Integer, UUID> ownerItemTilePositionMap;

    private ListView borrowerItemListView;
//    private EnhancedSimpleAdapter borrowerItemAdapter;
    private List<HashMap<String, Object>> borrowerItemTiles;
    private HashMap<Integer, UUID> borrowerItemTilePositionMap;


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

        ownerUsername = (TextView) findViewById(R.id.tradeComposeOtherUser);
        offerButton = (Button) findViewById(R.id.tradeComposeOffer);
        cancelButton = (Button) findViewById(R.id.tradeComposeCancel);
        addItemButton = (Button) findViewById(R.id.tradeComposeAddItem);

        ownerItemListView = (ListView) findViewById(R.id.tradeComposeOwnerItem);
        borrowerItemListView = (ListView) findViewById(R.id.tradeComposeBorrowerItems);

        ownerItemTilePositionMap = new HashMap<>();
        borrowerItemTilePositionMap = new HashMap<>();

        AsyncTask worker = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Context c = (Context) params[0];
                model = new Trade(Parcels.<User>unwrap(getIntent().getParcelableExtra("trade/compose/borrower")),
                        Parcels.<User>unwrap(getIntent().getParcelableExtra("trade/compose/owner")),
                        new ArrayList<Item>(),
                        new ArrayList<Item>() {{
                            add(Parcels.<Item>unwrap(getIntent().getParcelableExtra("trade/compose/item")));
                        }},
                        c);
                try {
                    model.getBorrower().getTradeList().addTrade(model);
                    model.getOwner().getTradeList().addTrade(model);
                } catch (IOException e) {
                    throw new RuntimeException("Primary User failed to get TradeList");
                } catch (ServiceNotAvailableException e) {
                    // todo make a snackbar toast instead of runtime exception
                    throw new RuntimeException("App is offline.", e);
                }
                controller = new TradeOfferComposeController(c, model);

                TileBuilder tileBuilder = new TileBuilder(getResources());
                ownerItemTiles = tileBuilder.buildItemTiles(model.getOwnersItems(), ownerItemTilePositionMap);
                borrowerItemTiles = tileBuilder.buildItemTiles(model.getBorrowersItems(), borrowerItemTilePositionMap);

                return c;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                final Context c = (Context) o;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] from = {"tileViewItemName", "tileViewItemCategory", "tileViewItemImage"};
                        int[] to = {R.id.tileViewItemName, R.id.tileViewItemCategory, R.id.tileViewItemImage};

//                        ownerItemAdapter = new EnhancedSimpleAdapter(c, ownerItemTiles, R.layout.fragment_item_tile, from, to);
//                        ownerItemListView.setAdapter(ownerItemAdapter);
//
//                        borrowerItemAdapter = new EnhancedSimpleAdapter(c, borrowerItemTiles, R.layout.fragment_item_tile, from, to);
//                        borrowerItemListView.setAdapter(borrowerItemAdapter);

                        addItemButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar.make(v, "add item to trade offer unimplemented", Snackbar.LENGTH_SHORT).show();
                                createAddTradeItemDialog().show();
                            }
                        });

                        ownerUsername.setText(model.getOwner().getUsername());
                        offerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AsyncTask worker = new AsyncTask() {
                                    @Override
                                    protected Object doInBackground(Object[] params) {
                                        controller.offerTrade();
                                        return null;
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
                                    @Override
                                    protected Object doInBackground(Object[] params) {
                                        controller.cancelTrade();
                                        return null;
                                    }
                                };
                                worker.execute();
                                activity.finish();
                            }
                        });
                    }
                });
            }
        };
        worker.execute(getBaseContext());
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

    private AlertDialog createAddTradeItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogContent = View.inflate(this, R.layout.content_add_search_dialog, null);

        //builder.setView(dialogContent); //todo replace with layout
        builder.setTitle("Add Trade Item");
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //todo Add Trade Entity item(s) to actual trade.
            }
        });


        ArrayList<String> inventoryItems = new ArrayList<String>();

        ArrayList<Item> theItems = new ArrayList<>();
        //todo Async grab this list
//        ArrayList<Item> theItems = model.getBorrowersItems();


        for (Item item : theItems) {
            inventoryItems.add(item.getItemName());
        }
        final CharSequence[] tradableItems = inventoryItems.toArray(new String[inventoryItems.size()]);
        builder.setItems(tradableItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = tradableItems[item].toString();
                //todo Highlight Item(s) Add Item to Trade Entity
            }
        });

        AlertDialog d = builder.create();
        return d;
    }
}
