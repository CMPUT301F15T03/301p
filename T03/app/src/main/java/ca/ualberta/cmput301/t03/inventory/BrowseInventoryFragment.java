package ca.ualberta.cmput301.t03.inventory;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link BrowseInventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseInventoryFragment extends Fragment implements Observer {
    private BrowsableInventories model;
    private BrowseInventoryController controller;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FloatingActionButton fab;
    private User user;
//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static BrowseInventoryFragment newInstance() {
        return new BrowseInventoryFragment();
    }

    public BrowseInventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse_inventory, container, false);


        Configuration c = new Configuration(getActivity().getApplicationContext());
        try{
            user = new User(c.getApplicationUserName(), getActivity().getApplicationContext());
        } catch (Exception e){

        }


        createListView(v);

        setupFab();

        return v;
    }

    private ArrayList<HashMap<String, String>> buildTiles() {
        ArrayList<HashMap<String, String>> tiles = new ArrayList<>();
        Item[] itemList = {new Item("test", "test"), new Item("test", "test"), new Item("test", "test"), new Item("test", "test"), new Item("test", "test") };
        //SHOULD BE REPLACED WITH ONCE LINKED ArrayList<Item> itemList = model.getBrowsables();
        for (Item item: itemList){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("tileViewItemName", item.getItemName());
            hm.put("tileViewItemCategory", item.getItemCategory());
            tiles.add(hm);
        }
        return tiles;
    }
    public void createListView(View v){
        final ListView listview = (ListView) v.findViewById(R.id.BrowseListView);
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
        fab.setImageDrawable(ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.ic_filter));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                controller.addFilter();
                Toast.makeText(getActivity().getBaseContext(), "ADD FILTER", Toast.LENGTH_SHORT).show();
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
