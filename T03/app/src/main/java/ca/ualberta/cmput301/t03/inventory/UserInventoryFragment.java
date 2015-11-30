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
import android.support.design.widget.FloatingActionButton;
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


import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
 * Fragment that displays the User's Inventory in a ListView.
 * <p>
 * By clicking the listed Items here the user can inspect, edit and delete.
 * The User can add an item by pressing the FloatingActionButton.
 */
public class UserInventoryFragment extends Fragment implements Observer, SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "user";
    Activity mActivity;
    View mView;
    private Inventory model;
    private UserInventoryController controller;
    private User user;

    private FloatingActionButton addItemFab;
    private ListView listview;
    private ItemsAdapter<Inventory> adapter;

    private HashMap<Integer, UUID> positionMap;
    List<HashMap<String, Object>> tiles;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ArrayList<FilterCriteria> filters;

    public UserInventoryFragment() {
        // Required empty public constructor
    }

    public static UserInventoryFragment newInstance(User u) {
        UserInventoryFragment fragment = new UserInventoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, Parcels.wrap(u));

        fragment.setArguments(args);
        return fragment;
    }


    public static UserInventoryFragment newInstance() {
        return new UserInventoryFragment();
    }

    /**
     * Gets User associated to system config. Loads any data associated to user.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        positionMap = new HashMap<>();
        filters = new ArrayList<>();
        setHasOptionsMenu(true);

        if (getArguments() != null) {
//            String username = getArguments().getString(ARG_PARAM1);
            user = Parcels.unwrap(getArguments().getParcelable(ARG_PARAM1));
            if (PrimaryUser.getInstance().equals(user)) {
                user = PrimaryUser.getInstance();
            } else {
//                user = new User(user.getUsername(), getActivity().getApplicationContext());
                try {
                    user = PrimaryUser.getInstance().getFriends().getFriend(user.getUsername());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("OFFLINE CANT DO THIS I GUESS");
                }
            }
        } else {
            user = PrimaryUser.getInstance();
        }


        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void[] params) {
                try {
//                    user = PrimaryUser.getInstance();

                    model = user.getInventory();
                    if (!PrimaryUser.getInstance().equals(user)) {
                        addFilter(new PrivateFilterCriteria());
                    }
                    controller = new UserInventoryController(getContext(), model);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void o) {
                fragmentSetup(getView());
                setupFab(getView());
            }
        };
        task.execute();

    }

    @Override
    public void onResume() {

        onRefresh();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_user_inventory, container, false);
        mView = v;
        setupSwipeRefresh(v);
        return v;
    }

    /**
     * Starts activity used to create a new item
     */
    public void addItemButtonClicked() {
        Intent intent = new Intent(getContext(), AddItemView.class);
        startActivity(intent);
    }

    private void fragmentSetup(View v) {
        createListView(v);
    }

    /**
     * Creates ListView Adapter and Item onClickListeners
     * This represents the users inventory.
     * @param v
     */
    public void createListView(View v) {
        final View view = v;

        listview = (ListView) v.findViewById(R.id.InventoryListView);
        adapter = new ItemsAdapter<>(getContext(), model);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inspectItem((Item) listview.getItemAtPosition(position));
            }
        });
    }

    /**
     * Starts activity in which an Item Can be inspected
     *
     * @param item
     */
    public void inspectItem(Item item) {

        Intent intent = null;
        if (PrimaryUser.getInstance().equals(user)) {
            intent = new Intent(getContext(), EditItemView.class);
            intent.putExtra("user", user.getUsername());
            intent.putExtra("ITEM_UUID", item.getUuid().toString());

        } else {
            intent = new Intent(getContext(), InspectItemView.class);
//            intent.putExtra("user", Parcels.wrap(user));
//            intent.putExtra("inventory/inspect/item", Parcels.wrap(item));

            // new and improved way
            intent.putExtra("user", user.getUsername());
            intent.putExtra("ITEM_UUID", item.getUuid().toString());
        }

        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (model != null) {
            model.removeObserver(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu_items, menu);
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

    public AlertDialog createAddFilterDialog() {
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

    public AlertDialog createAddSearchDialog() {
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

    private void setupFab(View v) {
        addItemFab = (FloatingActionButton) v.findViewById(R.id.addItemInventoryFab);
        if (PrimaryUser.getInstance().equals(user)) {
            addItemFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addItemButtonClicked();
                }
            });
        } else {
            addItemFab.hide();
        }
    }

    @Override
    public void update(Observable observable) {
        onRefresh();
    }

    public void setupSwipeRefresh(View v){
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.userInventorySwipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    PrimaryUser.getInstance().refresh();
                    model = user.getInventory();



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                model.setFilters(filters);
                adapter.notifyUpdated(model);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
        task.execute();
    }
}
