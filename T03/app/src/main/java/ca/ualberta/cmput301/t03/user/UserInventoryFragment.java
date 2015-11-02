package ca.ualberta.cmput301.t03.user;

import android.app.Activity;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.user.UserInventoryController;
import ca.ualberta.cmput301.t03.inventory.Inventory;


public class UserInventoryFragment extends Fragment implements Observer {
    private Inventory model;
    private UserInventoryController controller;

    private FloatingActionButton fab;
    private User user;

    // TODO: Rename and change types and number of parameters
    public static UserInventoryFragment newInstance() {
        return  new UserInventoryFragment();
    }

    public UserInventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_inventory, container, false);



        Configuration c = new Configuration(getActivity().getApplicationContext());
        try{
            user = new User(c.getApplicationUserName(), getActivity().getApplicationContext());
            model = user.getInventory();
        } catch (Exception e){
            throw new RuntimeException();
        }

        createListView(v);
        setupFab();

        return v;
    }

    private ArrayList<HashMap<String, String>> buildTiles() {
        ArrayList<HashMap<String, String>> tiles = new ArrayList<>();
//        Item[] itemList = {new Item("test", "test"), new Item("test", "test"), new Item("test", "test"), new Item("test", "test"), new Item("test", "test") };
        //SHOULD BE REPLACED WITH ONCE LINKED
        ArrayList<Item> itemList = model.getItems();
        for (Item item: itemList){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("tileViewItemName", item.getItemName());
            hm.put("tileViewItemCategory", item.getItemCategory());
            tiles.add(hm);
        }
        return tiles;
    }

    public void createListView(View v){
        final ListView listview = (ListView) v.findViewById(R.id.InventoryListView);
        List<HashMap<String,String>> tiles = buildTiles();
        String[] from = {"tileViewItemName", "tileViewItemCategory"};
        int[] to = {R.id.tileViewItemName, R.id.tileViewItemCategory};
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), tiles, R.layout.fragment_item_tile, from, to);
        listview.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        teardownFab();
    }

    private void setupFab(){
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.ic_add));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                controller.addItemButtonClicked();
                Toast.makeText(getActivity().getBaseContext(), "GET ITEM", Toast.LENGTH_SHORT).show();
            }
        });
        fab.show();
    }

    private void teardownFab(){
        fab.setOnClickListener(null);
        fab.hide();
    }

    @Override
    public void update(Observable observable) {
        throw new UnsupportedOperationException();
    }

}
