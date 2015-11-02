package ca.ualberta.cmput301.t03.user;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;


//https://guides.codepath.com/android/using-the-recyclerview

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link FriendsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsListFragment extends Fragment implements Observer {
    private FriendsList mModel;
    private FriendsListController mController;
    private RecyclerView mRecyclerView;
    private User mUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private FloatingActionButton fab;
    private ListView mListView;

//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsListFragment newInstance() {
        FriendsListFragment fragment = new FriendsListFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FriendsListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_friends_list, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);




//        Configuration c = new Configuration(getContext());


//        try {
//           mUser = new User(c.getApplicationUserName(), getContext());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        Collection<User> users = null;
//        try {
//            users = mUser.getFriends().getFriends();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        ArrayAdapter

        ArrayList myFriends = new ArrayList();
        myFriends.add("kyle");
//        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.id.friendsListRecyclerView);
//
//        new ArrayAdapter<String>();
//
//        mRecyclerView.setAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        teardownFab();
    }

    private void setupFab(){
        fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new AddFriendButtonOnClickListener());
        fab.setImageResource(R.drawable.ic_add_white_24dp);
        fab.show();
    }

    private void teardownFab(){
        fab.setOnClickListener(null);
        fab.hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupFab();


//        setupRecyclerView();

        setupListView();

    }

    private void setupListView(){
        mListView = (ListView) getActivity().findViewById(R.id.friendsListListView);

        FriendsList friendsList = new FriendsList();
        try {
            friendsList.addFriend(new User("steve", getContext()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

//        FriendsListListAdapter adapter = new FriendsListListAdapter(getContext(), friendsList);


        ArrayAdapter<User> adapter = new ArrayAdapter<User>(getContext(), R.layout.friends_list_item, friendsList.getFriends());
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) mListView.getItemAtPosition(position);

                Snackbar.make(getView(), user.getUsername(), Snackbar.LENGTH_SHORT).show();

            }
        });

    }


//    private void setupRecyclerView() {
//        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.friendsListRecyclerView);
//
//        FriendsList friendsList = new FriendsList();
//
//        try {
//            friendsList.addFriend(new User("steve", getContext()));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        FriendsListAdapter adapter = new FriendsListAdapter(friendsList);
//
//        mRecyclerView.setAdapter(adapter);
//
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
////            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public void update(Observable observable) {
        throw new UnsupportedOperationException();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

}
