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

package ca.ualberta.cmput301.t03.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.inventory.ItemTileFragment;
import ca.ualberta.cmput301.t03.user.UserInventoryController;
import ca.ualberta.cmput301.t03.inventory.Inventory;


public class UserInventoryFragment extends Fragment implements Observer {
    private Inventory model;
    private UserInventoryController controller;
    private User user;

    private FloatingActionButton addItemFab;
    private ListView listview;
    private SimpleAdapter adapter;

    private HashMap<Integer, UUID> positionMap;

    public UserInventoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserInventoryFragment newInstance() {
        return  new UserInventoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        positionMap = new HashMap<>();

        final Configuration c = new Configuration(getContext());
        c.getApplicationUserName();


        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    user = PrimaryUser.getInstance();

                    model = user.getInventory();
                    controller = new UserInventoryController(getContext(), model);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fragmentSetup(getView());
                        }
                    });



                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        worker.start();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_user_inventory, container, false);

        return v;
    }

    private void fragmentSetup(View v){
        createListView(v);
        setupFab(v);
        model.addObserver(this);


//        final Item itemModel = new Item();
//        itemModel.setItemName("lala");
//        itemModel.setItemQuantity(1);
//        itemModel.setItemQuality("good");
//        itemModel.setItemCategory("good");
//        itemModel.setItemIsPrivate(true);
//        itemModel.setItemDescription("lala");
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                model.addItem(itemModel);
//            }
//        });
//        thread.start();

    }

    private ArrayList<HashMap<String, String>> buildTiles() {
        ArrayList<HashMap<String, String>> tiles = new ArrayList<>();
//        Item[] itemList = {new Item("test", "test"), new Item("test", "test"), new Item("test", "test"), new Item("test", "test"), new Item("test", "test") };
        //SHOULD BE REPLACED WITH ONCE LINKED
        //HashMap<UUID, Item> tempMap = model.getItems();
        //Collection<Item> tempcollection = tempMap.values();
        //ArrayList<Item> itemList = (ArrayList<Item>) model.getItems().values();
        int i = 0;
        positionMap.clear();
        for (Item item: model.getItems().values()){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("tileViewItemName", item.getItemName());
            hm.put("tileViewItemCategory", item.getItemCategory());
            tiles.add(hm);
            positionMap.put(i, item.getUuid());
            i++;
        }
        return tiles;
    }

    public void createListView(View v){
        listview = (ListView) v.findViewById(R.id.InventoryListView);
        List<HashMap<String,String>> tiles = buildTiles();
        String[] from = {"tileViewItemName", "tileViewItemCategory"};
        int[] to = {R.id.tileViewItemName, R.id.tileViewItemCategory};
        adapter = new SimpleAdapter(getActivity().getBaseContext(), tiles, R.layout.fragment_item_tile, from, to);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controller.inspectItem(model.getItems().get(positionMap.get(position)));
                Toast.makeText(getActivity().getBaseContext(), "Inspect Item", Toast.LENGTH_SHORT).show();
                
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setupFab(View v){
        addItemFab = (FloatingActionButton) v.findViewById(R.id.addItemInventoryFab);
        addItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.addItemButtonClicked();
                Toast.makeText(getActivity().getBaseContext(), "ADD ITEM", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void update(Observable observable) {
        Log.d("Q", "Updating Inven Frag");
        onCreate(new Bundle());
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                adapter.notifyDataSetChanged();
//            }
//        });
//        throw new UnsupportedOperationException();
    }

}
