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

    public TopTradersFragment() {

    }

    public static TopTradersFragment newInstance() {
        return new TopTradersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshController = new TopTradersProvider(TradeApp.getInstance().createQueryExecutor());
        setHasOptionsMenu(true);
    }

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