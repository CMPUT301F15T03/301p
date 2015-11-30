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

package ca.ualberta.cmput301.t03.trading.toptraders;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.support.v4.app.Fragment;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.exceptions.ExceptionUtils;

/**
 * The view for the Top Traders feature. Shows the current list of top 10 traders.
 * Source (Icon): https://developers.google.com/games/services/images/branding/ic_play_games_badge_leaderboards_gray.png
 * Accessed: 28 Nov
 * Created by rishi on 15-11-28.
 */
public class TopTradersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int TOP_TRADERS_COUNT = 10;

    private TopTradersProvider refreshController;
    private TableLayout topTradersLayout;

    private ArrayList<TopTrader> topTraders;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * The default constructor needed for fragments.
     */
    public TopTradersFragment() {

    }

    /**
     * Creates a new instance of {@link TopTradersFragment}.
     * @return A newly constructed {@link TopTradersFragment}.
     */
    public static TopTradersFragment newInstance() {
        return new TopTradersFragment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshController = new TopTradersProvider(TradeApp.getInstance().createQueryExecutor());
        setHasOptionsMenu(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_traders, container, false);
        topTradersLayout = (TableLayout) rootView.findViewById(R.id.top_traders_grid);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.TopTradersSwipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        onRefresh();
        return rootView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRefresh() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    topTraders = refreshController.getTopTraders(TOP_TRADERS_COUNT);
                    refreshTopTradersUI();
                } catch (IOException e) {
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ExceptionUtils.toastShort(getString(R.string.top_trader_offline_message));
                            }
                        });
                    }

                }
            }
        });

        thread.start();
    }

    private void refreshTopTradersUI() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    topTradersLayout.removeAllViews();

                    for (TopTrader trader : topTraders) {
                        TableRow row = (TableRow) LayoutInflater.from(TopTradersFragment.this.getContext())
                                .inflate(R.layout.attrib_row, null);
                        ((TextView) row.findViewById(R.id.attrib_name)).setText(trader.getUserName());
                        ((TextView) row.findViewById(R.id.attrib_value)).setText(trader.getSuccessfulTradeCount().toString());
                        topTradersLayout.addView(row);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
}
