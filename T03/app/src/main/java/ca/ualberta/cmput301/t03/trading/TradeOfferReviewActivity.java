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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.exceptions.ExceptionUtils;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.inventory.ItemsAdapter;


/**
 * class TradeOfferReviewActivity is the View for reviewing a trade which
 * has been offered to the current user.
 * <p>
 * On interaction, it delegates to a {@link TradeOfferReviewController}.
 */
public class TradeOfferReviewActivity extends AppCompatActivity implements Observer {

    private static final Integer EMAIL_SENT = 1;

    private Activity activity = this;

    private Trade model;
    private TradeOfferReviewController controller;

    private String currentUsername;

    private TextView tradeDirectionFromTo;
    private TextView tradeReviewOtherUser;
    private TextView tradeOffererYouTheyWant;
    private TextView tradeOffererYouTheyOffer;

    private ListView ownerItemListView;
    private ListView borrowerItemListView;

    private Button tradeReviewAccept;
    private Button tradeReviewDecline;
    private Button tradeReviewDeclineAndCounterOffer;

    private ItemsAdapter ownerItemAdapter;
    private ItemsAdapter borrowerItemAdapter;

    private Inventory ownerItems;
    private Inventory borrowerItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_offer_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tradeDirectionFromTo = (TextView) findViewById(R.id.tradeDirectionFromTo);
        tradeReviewOtherUser = (TextView) findViewById(R.id.tradeReviewOtherUser);
        tradeOffererYouTheyWant = (TextView) findViewById(R.id.tradeOffererYouTheyWant);
        tradeOffererYouTheyOffer = (TextView) findViewById(R.id.tradeOffererYouTheyOffer);

        ownerItemListView = (ListView) findViewById(R.id.tradeReviewOwnerItem);
        borrowerItemListView = (ListView) findViewById(R.id.tradeReviewBorrowerItems);

        tradeReviewAccept = (Button) findViewById(R.id.tradeReviewAccept);
        tradeReviewDecline = (Button) findViewById(R.id.tradeReviewDecline);
        tradeReviewDeclineAndCounterOffer = (Button) findViewById(R.id.tradeReviewDeclineAndCounterOffer);

        UUID tradeUUID = (UUID) getIntent().getSerializableExtra("TRADE_UUID");
        populateLayoutWithData(tradeUUID);
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

    private void populateLayoutWithData(final UUID tradeUUID) {


        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            private ArrayList<Item> owneritems;
            private ArrayList<Item> borroweritems;
            private Boolean currentUserOwnsMainItem;
            private Boolean tradeIsPending;
            private Boolean tradeIsAccepted;

            @Override
            protected Void doInBackground(Void[] params) {
                try {
                    model = PrimaryUser.getInstance().getTradeList().getTrades().get(tradeUUID);
                    currentUserOwnsMainItem = model.getOwner().getUsername().equals(currentUsername);
                    TradeState state = model.getState();
                    tradeIsPending = state.getClass().equals(TradeStateOffered.class);
//                    tradeIsPending = mode
                } catch (IOException e) {
                    ExceptionUtils.toastErrorWithNetwork();
                    activity.finish();
                } catch (ServiceNotAvailableException e) {
                    ExceptionUtils.toastLong("Trade operations are unavailable offline");
                    activity.finish();
                }


                if (model == null) {
                    ExceptionUtils.toastLong("Failed to fetch trade info");
                    activity.finish();
                }
                ownerItems = model.getOwnersItems();
                ownerItems.addObserver(TradeOfferReviewActivity.this);


                try {
                    borrowerItems = model.getBorrowersItems();
                    borrowerItems.addObserver(TradeOfferReviewActivity.this);
                } catch (ServiceNotAvailableException e) {
                    ExceptionUtils.toastLong("Failed to get borrowers items: app is offline");
                    activity.finish();
                }


                controller = new TradeOfferReviewController(getBaseContext(), model);

                currentUsername = PrimaryUser.getInstance().getUsername();

                return null;
            }

            @Override
            protected void onPostExecute(Void o) {
                if (tradeIsPending) {
                    populateLayoutPendingTrade(currentUserOwnsMainItem);
                } else if (tradeIsAccepted) {
//                    populateLayoutAcceptedTrade(currentUserOwnsMainItem);
//                } else if (tradeIsCompleted) {
//
                }

                ownerItemAdapter = new ItemsAdapter(TradeApp.getContext(), owneritems);
                borrowerItemAdapter = new ItemsAdapter(TradeApp.getContext(), borroweritems);

                ownerItemListView.setAdapter(ownerItemAdapter);
                borrowerItemListView.setAdapter(borrowerItemAdapter);
            }
        };

        task.execute();
    }

    private void populateLayoutPendingTrade(Boolean currentUserOwnsMainItem) {
        if (currentUserOwnsMainItem) {
            tradeDirectionFromTo.setText("from");
            tradeReviewOtherUser.setText(model.getBorrower().getUsername());
            tradeOffererYouTheyWant.setText("They");
            tradeOffererYouTheyOffer.setText("They");
            tradeReviewAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AsyncTask task = new AsyncTask() {
                        String emailOwner;
                        String emailBorrower;
                        Boolean emailUsers = false;

                        @Override
                        protected Object doInBackground(Object[] params) {
                            try {
                                controller.acceptTrade();
                                emailBorrower = model.getBorrower().getProfile().getEmail();
                                emailOwner = model.getOwner().getProfile().getEmail();
                                emailUsers = true;
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ServiceNotAvailableException e) {
                                ExceptionUtils.toastLong("Failed to accept trade: app is offline");
                                activity.finish();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            if (emailUsers) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/rfc822");
                                intent.putExtra(Intent.EXTRA_EMAIL,
                                        new String[]{
                                                emailOwner,
                                                emailBorrower
                                        });
                                intent.putExtra(Intent.EXTRA_SUBJECT, model.getEmailSubject());
                                intent.putExtra(Intent.EXTRA_TEXT, model.getEmailBody());
                                try {
                                    startActivityForResult(intent, EMAIL_SENT);
                                } catch (ActivityNotFoundException e) {
                                    ExceptionUtils.toastLong("No email client installed");
                                    activity.finish();
                                }
                            } else {
                                activity.finish();
                            }
                        }
                    };
                    task.execute();
                }
            });
            tradeReviewDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AsyncTask task = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] params) {
                            try {
                                controller.declineTrade();
                            } catch (ServiceNotAvailableException e) {
                                ExceptionUtils.toastLong("Failed to decline trade: app is offline");
                            }
                            return null;
                        }
                    };
                    task.execute();
                    activity.finish();
                }
            });
            tradeReviewDeclineAndCounterOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExceptionUtils.toastShort("trade decline&counter-offer unimplemented");
                }
            });
        } else {
            // !currentUserOwnsMainItem
            tradeDirectionFromTo.setText("to");
            tradeReviewOtherUser.setText(model.getOwner().getUsername());
            tradeOffererYouTheyWant.setText("You");
            tradeOffererYouTheyOffer.setText("You");
            tradeReviewAccept.setVisibility(View.GONE);
            tradeReviewDecline.setVisibility(View.GONE);
            tradeReviewDeclineAndCounterOffer.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trade_offer_review, menu);
        return true;
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMAIL_SENT) {
            activity.finish();
        }
    }

    @Override
    public void update(final Observable observable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                borrowerItemAdapter.notifyDataSetChanged();
                ownerItemAdapter.notifyDataSetChanged();
            }
        });

    }
}
