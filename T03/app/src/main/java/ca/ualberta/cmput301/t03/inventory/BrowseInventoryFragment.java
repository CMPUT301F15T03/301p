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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.photo.Photo;
import ca.ualberta.cmput301.t03.user.User;

/**
 * Fragment that displays a ListView containing all Items from all friends.
 * This can (later) be filtered by friends, category, and Texttual Query.
 */
public class BrowseInventoryFragment extends Fragment implements Observer {
    Activity mActivity;
    View mView;

    ArrayList<Item> allItems;
    ArrayList<HashMap<String, Object>> listItems;
    EnhancedSimpleAdapter adapter;

    private BrowsableInventories model;
    private BrowseInventoryController controller;

    private FloatingActionButton addFilterBrowseFab;
    private User user;

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

        try {
            user = PrimaryUser.getInstance();
            model = new BrowsableInventories(); //FIXME this seems fishy
            controller = new BrowseInventoryController(getContext(), model);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        listItems = new ArrayList<>();
        allItems = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse_inventory, container, false);
        mView = v;

        ListView listview = (ListView) v.findViewById(R.id.BrowseListView);
        String[] from = {"tileViewItemName", "tileViewItemCategory", "tileViewItemImage"};
        int[] to = {R.id.tileViewItemName, R.id.tileViewItemCategory, R.id.tileViewItemImage};
        adapter = new EnhancedSimpleAdapter(mActivity.getBaseContext(), listItems, R.layout.fragment_item_tile, from, to);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inspectItem(allItems.get(position));
            }
        });

        setupListView();

        return v;
    }


    private void setupListView() {
        Thread tGetBrowsables = model.getBrowsables();
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(model.getConstructorThread());
        pool.submit(tGetBrowsables);

        Thread tAddToListView = new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        allItems = model.getList();
                        for (Item item : model.getList()) {
                            addToListView(item);
                        }
                    }
                });
            }
        });
        pool.submit(tAddToListView);
        pool.shutdown();
    }

    /**
     * Used to add Items to the ListView.
     *
     * @param item
     */
    public void addToListView(Item item) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("tileViewItemName", item.getItemName());
        hm.put("tileViewItemCategory", item.getItemCategory());
        if (item.getPhotoList().getPhotos().size() > 0 ) {
            hm.put("tileViewItemImage", (Bitmap) item.getPhotoList().getPhotos().get(0).getPhoto());
        }
        else {
            hm.put("tileViewItemImage", ((BitmapDrawable) getResources().getDrawable(R.drawable.photo_unavailable)).getBitmap());
        }
        listItems.add(hm);
        adapter.notifyDataSetChanged();
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
                intent.putExtra("inventory/inspect/item", Parcels.wrap(items[0]));
                try {
                    for (User friend : PrimaryUser.getInstance().getFriends().getFriends()) {
                        if (friend.getInventory().getItems().containsKey(items[0].getUuid())) {
                            intent.putExtra("user", Parcels.wrap(friend));
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
        super.onDestroy();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_browse_inventory, menu);
    }

    @Override
    public void update(Observable observable) {
        throw new UnsupportedOperationException();
    }
}
