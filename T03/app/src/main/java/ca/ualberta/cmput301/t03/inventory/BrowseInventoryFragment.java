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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.user.User;
/**
 * Fragment that displays a ListView containing all Items from all friends.
 * This can (later) be filtered by friends, category, and Texttual Query.
 */
public class BrowseInventoryFragment extends Fragment implements Observer {
    Activity mActivity;
    View mView;

    ArrayList<Item> allItems;
    ArrayList<HashMap<String,String>> listItems;
    SimpleAdapter adapter;

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
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

        Configuration c = new Configuration(getActivity().getApplicationContext());
        try{
            user = PrimaryUser.getInstance();
            model = new BrowsableInventories();
            controller = new BrowseInventoryController(getContext(), model);
        } catch (Exception e){
            throw new RuntimeException();
        }
        listItems = new ArrayList<HashMap<String,String>>();
        allItems = new ArrayList<Item>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse_inventory, container, false);
        mView = v;

        ListView listview = (ListView) v.findViewById(R.id.BrowseListView);
        String[] from = {"tileViewItemName", "tileViewItemCategory"};
        int[] to = {R.id.tileViewItemName, R.id.tileViewItemCategory};
        adapter = new SimpleAdapter(mActivity.getBaseContext(), listItems, R.layout.fragment_item_tile, from, to);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inspectItem(allItems.get(position));
                Toast.makeText(mActivity.getBaseContext(), "Inspect Item", Toast.LENGTH_SHORT).show();

            }
        });

        setupListView();
        setupFab(mView);

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
                        for(Item item: allItems){
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
     * @param item
     */
    public void addToListView(Item item){
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("tileViewItemName", item.getItemName());
        hm.put("tileViewItemCategory", item.getItemCategory());
        listItems.add(hm);
        adapter.notifyDataSetChanged();
    }

    /**
     * Starts intent for inspecting item
     * @param item
     */
    public void inspectItem(Item item){

        Intent intent = new Intent(getContext(), InspectItemView.class);
        intent.putExtra("user", Parcels.wrap(user));
        intent.putExtra("inventory/inspect/item", Parcels.wrap(item));

        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setupFab(View v){
        addFilterBrowseFab = (FloatingActionButton) v.findViewById(R.id.addFilterBrowseFab);
        addFilterBrowseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                controller.addFilter();
                Toast.makeText(getActivity().getBaseContext(), "ADD FILTER", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void update(Observable observable) {
        throw new UnsupportedOperationException();
    }
}
