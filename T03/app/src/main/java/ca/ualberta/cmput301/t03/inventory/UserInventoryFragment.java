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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.user.User;


public class UserInventoryFragment extends Fragment implements Observer {
    Activity mActivity;
    View mView;

    private static final String ARG_PARAM1 = "user";

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

    public static UserInventoryFragment newInstance(User u){
        UserInventoryFragment fragment = new UserInventoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, Parcels.wrap(u));

        fragment.setArguments(args);
        return fragment;
    }
    public static UserInventoryFragment newInstance() {
        return  new UserInventoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

        positionMap = new HashMap<>();


        if (getArguments() != null) {
//            String username = getArguments().getString(ARG_PARAM1);
            user = Parcels.unwrap(getArguments().getParcelable(ARG_PARAM1));
            if(PrimaryUser.getInstance().equals(user)){
                user = PrimaryUser.getInstance();
            } else {
                user = new User(user.getUsername(), getActivity().getApplicationContext());
            }
        } else {
            user = PrimaryUser.getInstance();
        }


        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try{
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
                }
                return null;
            }
        };
        task.execute();

    }

    public void addItemButtonClicked() {
//        throw new UnsupportedOperationException();
        Intent intent = new Intent(getContext(), AddItemView.class);
        startActivity(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_user_inventory, container, false);
        mView = v;
        return v;
    }

    private void fragmentSetup(View v){
        createListView(v);
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
        adapter = new SimpleAdapter(mActivity.getBaseContext(), tiles, R.layout.fragment_item_tile, from, to);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inspectItem(model.getItems().get(positionMap.get(position)));
            }
        });

    }

    public void inspectItem(Item item){

        Intent intent = null;
        if (PrimaryUser.getInstance().equals(user)){
            intent = new Intent(getContext(), EditItemView.class);
            intent.putExtra("user", Parcels.wrap(user));
            intent.putExtra("ITEM_UUID", item.getUuid().toString());

        } else {
            intent = new Intent(getContext(), InspectItemView.class);
            intent.putExtra("user", Parcels.wrap(user));
            intent.putExtra("inventory/inspect/item", Parcels.wrap(item));
        }

        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setupFab(View v){
        addItemFab = (FloatingActionButton) v.findViewById(R.id.addItemInventoryFab);
        if (PrimaryUser.getInstance().equals(user)){
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
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                fragmentSetup(mView);
            }
        });
//        throw new UnsupportedOperationException();
    }

}
