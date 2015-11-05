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
import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.exceptions.ExceptionUtils;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.inventory.BrowsableInventories;
import ca.ualberta.cmput301.t03.inventory.BrowseInventoryController;
import ca.ualberta.cmput301.t03.user.AddFriendButtonOnClickListener;
import ca.ualberta.cmput301.t03.user.User;
/**
 * Copyright 2015 Quentin Lautischer
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link BrowseInventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseInventoryFragment extends Fragment implements Observer {
    Activity mActivity;
    View mView;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

        Configuration c = new Configuration(getActivity().getApplicationContext());
        try{
            user = new User(c.getApplicationUserName(), mActivity.getApplicationContext());
            model = new BrowsableInventories();
            controller = new BrowseInventoryController(getContext(), model);
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse_inventory, container, false);
        mView = v;
        setupFab(mView);
        createListView(mView);

        return v;
    }

    private ArrayList<HashMap<String, String>> buildTiles() {
        ArrayList<HashMap<String, String>> tiles = new ArrayList<>();
//        Item[] itemList = {new Item("test", "test"), new Item("test", "test"), new Item("test", "test"), new Item("test", "test"), new Item("test", "test") };
        ArrayList<Item> itemList = model.getBrowsables();
        for (Item item: itemList){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("tileViewItemName", item.getItemName());
            hm.put("tileViewItemCategory", item.getItemCategory());
            tiles.add(hm);
        }
        return tiles;
    }
    public void createListView(View v){
        ListView listview = (ListView) v.findViewById(R.id.BrowseListView);
        List<HashMap<String,String>> tiles = buildTiles();
        String[] from = {"tileViewItemName", "tileViewItemCategory"};
        int[] to = {R.id.tileViewItemName, R.id.tileViewItemCategory};
        SimpleAdapter adapter = new SimpleAdapter(mActivity.getBaseContext(), tiles, R.layout.fragment_item_tile, from, to);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getBaseContext(), "Inspect Item", Toast.LENGTH_SHORT).show();

                // TODO: actually get item clicked. this is for testing purposes
                Item itemTest = new Item("TestItem", "TestCategory");
                itemTest.setItemQuantity(1);
                itemTest.setItemQuality("Good");
                itemTest.setItemDescription("Pretty good");

                controller.inspectItem(itemTest);
            }
        });
    }

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
