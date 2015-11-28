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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.TileBuilder;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.photo.Photo;
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
    private InventoryAdapter adapter;

    private HashMap<Integer, UUID> positionMap;
    List<HashMap<String, Object>> tiles;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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


        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
//                    user = PrimaryUser.getInstance();

                    model = user.getInventory();
                    controller = new UserInventoryController(getContext(), model);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fragmentSetup(getView());
                            setupFab(getView());
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
                return null;
            }
        };
        task.execute();

    }

    @Override
    public void onResume() {

        fragmentSetup(mView);
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
        AsyncTask worker = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                TileBuilder tileBuilder = new TileBuilder(getResources());
                tiles = tileBuilder.buildItemTiles(model.getItems().values(), positionMap);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                listview = (ListView) view.findViewById(R.id.InventoryListView);
                String[] from = {"tileViewItemName", "tileViewItemCategory", "tileViewItemImage"};
                int[] to = {R.id.tileViewItemName, R.id.tileViewItemCategory, R.id.tileViewItemImage};
//                adapter = new EnhancedSimpleAdapter(mActivity.getBaseContext(), tiles, R.layout.fragment_item_tile, from, to);

                adapter = new InventoryAdapter(getContext(), model);

                listview.setAdapter(adapter);

                adapter.add(new Item("pizza", "cat"));

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        inspectItem(model.getItems().get(positionMap.get(position)));
                    }
                });

                super.onPostExecute(o);
            }
        };
        worker.execute();

//        listview = (ListView) v.findViewById(R.id.InventoryListView);
//        List<HashMap<String, Object>> tiles = buildTiles();
//        String[] from = {"tileViewItemName", "tileViewItemCategory", "tileViewItemImage"}; //
//        int[] to = {R.id.tileViewItemName, R.id.tileViewItemCategory, R.id.tileViewItemImage}; //
//        adapter = new EnhancedSimpleAdapter(mActivity.getBaseContext(), tiles, R.layout.fragment_item_tile, from, to);
//        listview.setAdapter(adapter);
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                inspectItem(model.getItems().get(positionMap.get(position)));
//            }
//        });

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
        model.removeObserver(this);
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
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                fragmentSetup(mView);
//                adapter.notifyDataSetChanged();
//            }
//        });
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

                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
        task.execute();
    }
}
