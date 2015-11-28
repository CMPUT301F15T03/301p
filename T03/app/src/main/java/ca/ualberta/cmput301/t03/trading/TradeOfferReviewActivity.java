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
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.TileBuilder;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.inventory.EnhancedSimpleAdapter;

/**
 * class TradeOfferReviewActivity is the View for reviewing a trade which
 * has been offered to the current user.
 * <p>
 * On interaction, it delegates to a {@link TradeOfferReviewController}.
 */
public class TradeOfferReviewActivity extends AppCompatActivity {

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

    private EnhancedSimpleAdapter ownerItemAdapter;
    private List<HashMap<String, Object>> ownerItemTiles;
    private HashMap<Integer, UUID> ownerItemTilePositionMap;

    private EnhancedSimpleAdapter borrowerItemAdapter;
    private List<HashMap<String, Object>> borrowerItemTiles;
    private HashMap<Integer, UUID> borrowerItemTilePositionMap;

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

    private void populateLayoutWithData(final UUID tradeUUID) {
        ownerItemTilePositionMap = new HashMap<>();
        borrowerItemTilePositionMap = new HashMap<>();

        AsyncTask<Void, Void, Context> task = new AsyncTask<Void, Void, Context>() {
            @Override
            protected Context doInBackground(Void[] params) {
                try {
                    model = PrimaryUser.getInstance().getTradeList().getTrades().get(tradeUUID);
                } catch (IOException e) {
                    Log.e("tradereview", e.getMessage());
                    throw new RuntimeException();
                } catch (ServiceNotAvailableException e) {
                    /**
                     * todo do something reasonable when offline and clicking on a trade, maybe go back
                     */
                    Log.e("tradereview", "Trade Review attempted while offline");
                    throw new RuntimeException("Trade Review attempted while offline");
                }

                TileBuilder tileBuilder = new TileBuilder(getResources());
                ownerItemTiles = tileBuilder.buildItemTiles(model.getOwnersItems(), ownerItemTilePositionMap);
                borrowerItemTiles = tileBuilder.buildItemTiles(model.getBorrowersItems(), borrowerItemTilePositionMap);

                currentUsername = PrimaryUser.getInstance().getUsername();

                return getBaseContext();
            }

            @Override
            protected void onPostExecute(final Context c) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Boolean currentUserOwnsMainItem = model.getOwner().getUsername().equals(currentUsername);
                        if (currentUserOwnsMainItem) {
                            tradeDirectionFromTo.setText("from");
                            tradeReviewOtherUser.setText(model.getBorrower().getUsername());
                            tradeOffererYouTheyWant.setText("They");
                            tradeOffererYouTheyOffer.setText("They");
                            tradeReviewAccept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Snackbar.make(v, "trade accept unimplemented", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                            tradeReviewDecline.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Snackbar.make(v, "trade decline unimplemented", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                            tradeReviewDeclineAndCounterOffer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Snackbar.make(v, "trade decline&counter-offer unimplemented", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            tradeDirectionFromTo.setText("to");
                            tradeReviewOtherUser.setText(model.getOwner().getUsername());
                            tradeOffererYouTheyWant.setText("You");
                            tradeOffererYouTheyOffer.setText("You");
                            tradeReviewAccept.setVisibility(View.GONE);
                            tradeReviewDecline.setVisibility(View.GONE);
                            tradeReviewDeclineAndCounterOffer.setVisibility(View.GONE);
                        }

                        String[] from = {"tileViewItemName", "tileViewItemCategory", "tileViewItemImage"};
                        int[] to = {R.id.tileViewItemName, R.id.tileViewItemCategory, R.id.tileViewItemImage};

                        ownerItemAdapter = new EnhancedSimpleAdapter(c, ownerItemTiles, R.layout.fragment_item_tile, from, to);
                        ownerItemListView.setAdapter(ownerItemAdapter);

                        borrowerItemAdapter = new EnhancedSimpleAdapter(c, borrowerItemTiles, R.layout.fragment_item_tile, from, to);
                        borrowerItemListView.setAdapter(borrowerItemAdapter);
                    }
                });
            }
        };

        task.execute();
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
}
