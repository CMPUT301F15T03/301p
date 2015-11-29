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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.TileBuilder;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.inventory.EnhancedSimpleAdapter;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeModificationException;

/**
 * View that shows the history of all past and pending trades for a user. Will observe the users
 * tradelist for new trades.
 */
public class TradeOfferHistoryFragment extends Fragment implements Observer {

    private TradeList model;

    private ListView listView;
    private EnhancedSimpleAdapter adapter;
    private List<HashMap<String, Object>> tradeTiles;
    private HashMap<Integer, UUID> tradeTilePositionMap;

    public TradeOfferHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TradeOfferHistoryFragment newInstance() {
        return new TradeOfferHistoryFragment();
    }

    /**
     * Sets up the view components for the view, and sets the current model.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tradeTilePositionMap = new HashMap<>();

        AsyncTask worker = new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // TODO show loading indicator
            }

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    model = PrimaryUser.getInstance().getTradeList();
                    for (Trade trade : model.getTradesAsList()) {
                        if (!trade.isPublic()) {
                            try {
                                model.remove(trade);
                            } catch (IllegalTradeModificationException e) {
                                Log.e("trade", "Non-public trades cannot be removed from trade lists");
                            }
                        }
                    }
                    model.commitChanges();
                    TileBuilder tileBuilder = new TileBuilder(getResources());
                    tradeTiles = tileBuilder.buildTradeTiles(model, tradeTilePositionMap);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // todo hide loading indicator
                        setupListView(getContext());
                        observeModel();
                    }
                });
            }
        };
        worker.execute();
    }

    /**
     * Sets up adapters for view elements representing trades
     */
    private void setupListView(Context context) {
        listView = (ListView) getActivity().findViewById(R.id.tradeHistoryListView);

        String[] from = {"tradeTileMainItemCategory",
                "tradeTileMainItemImage",
                "tradeTileMainItemName",
                "tradeTileOtherUser",
                "tradeTileTradeState"};
        int[] to = {R.id.tradeTileMainItemCategory,
                R.id.tradeTileMainItemImage,
                R.id.tradeTileMainItemName,
                R.id.tradeTileOtherUser,
                R.id.tradeTileTradeState};

        adapter = new EnhancedSimpleAdapter(context, tradeTiles, R.layout.fragment_trade_tile, from, to);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final UUID tradeUUID = tradeTilePositionMap.get(position);

                AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Void[] params) {
                        Boolean shouldReview = Boolean.TRUE;
                        try {
                            if (model.getTrades().get(tradeUUID).isClosed()) {
                                shouldReview = Boolean.FALSE;
                            }
                        } catch (ServiceNotAvailableException e) {
                            // TODO notify onPostExecute somehow that it should toast network failure
                        }
                        return shouldReview;
                    }

                    @Override
                    protected void onPostExecute(Boolean shouldReview) {
                        if (shouldReview) {
                            Intent intent = new Intent(getContext(), TradeOfferReviewActivity.class);
                            intent.putExtra("TRADE_UUID", tradeUUID);
                            startActivity(intent);
                        } else {
                            Snackbar.make(getView(), "trade review of accepted|declined trades unimplemented", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                };
                task.execute();
            }
        });
    }

    /**
     * setup observer pattern between view and model
     */
    private void observeModel() {
        model.addObserver(this);
    }

    /**
     * {@inheritDoc}
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade_offer_history, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void update(Observable observable) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

}
