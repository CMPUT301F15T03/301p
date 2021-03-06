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

package ca.ualberta.cmput301.t03.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.filters.FilterDialogs;
import ca.ualberta.cmput301.t03.filters.item_criteria.CategoryFilterCriteria;
import ca.ualberta.cmput301.t03.filters.item_criteria.PrivateFilterCriteria;
import ca.ualberta.cmput301.t03.filters.item_criteria.StringQueryFilterCriteria;
import ca.ualberta.cmput301.t03.user.User;


/**
 * Fragment that displays a ListView containing all Items from all friends.
 * This can (later) be filtered by friends, category, and Texttual Query.
 */
public class BrowseInventoryFragment extends Fragment implements Observer, SwipeRefreshLayout.OnRefreshListener {
    Activity mActivity;
    View mView;

    ArrayList<Item> allItems;
    ArrayList<HashMap<String, Object>> listItems;
    ItemsAdapter<Inventory> adapter;

    private Inventory model;
    private BrowseInventoryController controller;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListview;

    private ArrayList<FilterCriteria> filters;
    private final Object uiLock = new Object();
    private boolean init = true;

    public BrowseInventoryFragment() {
        // Required empty public constructor
    }

    public static BrowseInventoryFragment newInstance() {
        return new BrowseInventoryFragment();
    }

    /**
     * Gets User associated to system config. Loads any data associated to Browsables.
     * Namely Users friendList.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        filters = new ArrayList<>();
        filters.add(new PrivateFilterCriteria());
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        synchronized (uiLock) {
            mView = inflater.inflate(R.layout.fragment_browse_inventory, container, false);
            mListview = (ListView) mView.findViewById(R.id.BrowseListView);
        }
        return mView;
    }

    /**
     * Starts intent for inspecting item
     *
     * @param item
     */
    public void inspectItem(Item item) {
        AsyncTask<Item, Void, Intent> findItemOwnerAndStartInspectItemActivity = new AsyncTask<Item, Void, Intent>() {
            @Override
            protected Intent doInBackground(Item[] items) {
                Intent intent = new Intent(getContext(), InspectItemView.class);
                try {
                    for (User friend : PrimaryUser.getInstance().getFriends().getFriends()) {
                        if (friend.getInventory().getItems().containsKey(items[0].getUuid())) {
                            intent.putExtra("user", friend.getUsername());
                            intent.putExtra("ITEM_UUID", items[0].getUuid().toString());
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
                return intent;
            }

            @Override
            protected void onPostExecute(Intent i) {
                super.onPostExecute(i);
                startActivity(i);
            }
        };
        findItemOwnerAndStartInspectItemActivity.execute(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        if (model != null){
            model.removeObserver(this);
        }
        super.onDestroy();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.inventory_filter_menu_items, menu);
    }

    @Override
    public void update(Observable observable) {
        onRefresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        AlertDialog alertDialog;
        switch (item.getItemId()) {
            case R.id.filter_inventory_button:
                alertDialog = FilterDialogs.createAddFilterDialog(getContext(), filters, new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        onRefresh(); //Ideally like to not full refresh and just have the adapter watch the item list change
                        return null;
                    }
                });
                alertDialog.show();
                return true;
            case R.id.search_inventory_button:
                alertDialog = FilterDialogs.createAddSearchDialog(getContext(), filters, new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        onRefresh(); //Ideally like to not full refresh and just have the adapter watch the item list change
                        return null;
                    }
                });
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupListView() {
        adapter = new ItemsAdapter<>(mActivity.getBaseContext(), model);
        mListview.setAdapter(adapter);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inspectItem((Item) parent.getItemAtPosition(position));
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.browseListSwipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(BrowseInventoryFragment.this);
    }

    @Override
    public void onRefresh() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    PrimaryUser.getInstance().refresh();
                    model = PrimaryUser.getInstance().getBrowseableInventories(filters);
                    //fixme browseableinventories actually needs to refresh
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
//                model.setFilters(filters);
                if (model.size() > 0) {
                    if (init) {
                        init = false;
                        setupListView();
                    }
                    else {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        adapter.notifyUpdated(model);
                    }
                } else {
                    synchronized (uiLock) {
                        if (getActivity().getSupportFragmentManager() != null){
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContent, NoFriendsBrowseInventory.newInstance())
                                    .commit();
                        }
                    }
                }
            }
        };
        task.execute();
    }
}
