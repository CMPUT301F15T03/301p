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

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.filters.FilterCriteria;
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

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    model = PrimaryUser.getInstance().getBrowseableInventories(filters);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setupListView();
            }
        };
        task.execute();

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
        setupSwipeRefresh(getView());

    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse_inventory, container, false);
        mView = v;

        mListview = (ListView) v.findViewById(R.id.BrowseListView);



        return v;
    }


    private void setupSwipeRefresh(View v){
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.browseListSwipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
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
                    e.printStackTrace();
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
        inflater.inflate(R.menu.filter_menu_items, menu);
    }

    @Override
    public void update(Observable observable) {
        onRefresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.filter_inventory_button:
                createAddFilterDialog().show();
                return true;
            case R.id.search_inventory_button:
                createAddSearchDialog().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private AlertDialog createAddFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(  getContext());
        final View dialogContent = View.inflate(getContext(), R.layout.content_add_filter_dialog, null);

        builder.setView(dialogContent);
        //itemCategoryText.setSelection(((ArrayAdapter) itemCategoryText.getAdapter()).getPosition(itemModel.getItemCategory()));
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Iterator<FilterCriteria> i = filters.iterator();
                while(i.hasNext()){
                    FilterCriteria filter = i.next();
                    if (filter.getType().equals("category")) {
                        i.remove();
                    }
                }
                Spinner spinner = (Spinner) dialogContent.findViewById(R.id.itemFilterCategory);
                String categoryType = spinner.getSelectedItem().toString();
                if (!categoryType.toLowerCase().equals("none")) {
                    addFilter(new CategoryFilterCriteria(categoryType));
                    Toast.makeText(getContext(), "Category Filter: '" + categoryType + "'", Toast.LENGTH_SHORT).show();
                } else {
                    onRefresh();
                }
                Toast.makeText(getContext(), "Category Filter: '" + categoryType + "'", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setTitle("Set a Category Filter");
        AlertDialog d = builder.create();
        return d;
    }

    private AlertDialog createAddSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogContent = View.inflate(getContext(), R.layout.content_add_search_dialog, null);
        final EditText e = (EditText) dialogContent.findViewById(R.id.addSearchFilterText);

        builder.setView(dialogContent); //todo replace with layout

        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String usr = e.getText().toString().trim();
                addFilter(new StringQueryFilterCriteria(usr));
                Toast.makeText(getContext(), "Textual Filter: '" + usr + "' Added", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setTitle("Textual Filters");


        ArrayList<String> filterNames = new ArrayList<String>();
        for (FilterCriteria filter : filters) {
            if(filter.getType().equals("textual")){
                filterNames.add(filter.getName());
            }
        }
        final CharSequence[] fNames = filterNames.toArray(new String[filterNames.size()]);
        builder.setItems(fNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = fNames[item].toString();
                removeFilter(selectedText);

                Toast.makeText(getContext(), "Textual Filter: '" + selectedText + "' Removed", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog d = builder.create();
        return d;
    }

    public void addFilter(FilterCriteria filter){
        filters.add(filter);
        onRefresh();
    }

    public void removeFilter(String filterName){
//        for (FilterCriteria filter: filters){
//            if (filter.getType().equals("textual") && filter.getName().equals(filterName)){
//                filters.remove(filter);
//            }
//        }
        Iterator<FilterCriteria> i = filters.iterator();
        while(i.hasNext()){
            FilterCriteria filter = i.next();
            if (filter.getType().equals("textual") && filter.getName().equals(filterName)) {
                i.remove();
            }
        }
        onRefresh();
    }


    public void onRefresh() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    PrimaryUser.getInstance().refresh();
                    model = PrimaryUser.getInstance().getBrowseableInventories(filters);


                    //fixme browseableinventories actually needs to refresh
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyUpdated(model);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
        task.execute();
    }
}
