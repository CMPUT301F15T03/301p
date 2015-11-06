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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
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

    private FloatingActionButton addFriendFab;
    private ListView mListView;
    private ArrayAdapter<User> mAdapter;

    public FriendsListFragment() {
        // Required empty public constructor
    }

    public static FriendsListFragment newInstance() {
        FriendsListFragment fragment = new FriendsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        }
        setHasOptionsMenu(true);

        mUser = PrimaryUser.getInstance();

        AsyncTask worker = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    mModel = mUser.getFriends();
                    mController = new FriendsListController(getContext(), mModel);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateFields();
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
//
//        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.id.friendsListRecyclerView);
//
//        new ArrayAdapter<String>();
//
//        mRecyclerView.setAdapter();
    }

    public void populateFields() {
        //do the listview here.
        setupFab();
        setupListView();
        mModel.addObserver(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mModel!=null) mModel.removeObserver(this);
    }

    private void setupFab() {
        addFriendFab = (FloatingActionButton)getActivity().findViewById(R.id.addFriendFab);
        addFriendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog().show();
            }
        });
        addFriendFab.setImageResource(R.drawable.ic_add_white_24dp);
    }



    private AlertDialog createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final EditText e = new EditText(getContext());
        builder.setView(e); //todo replace with layout
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Add", null);
        builder.setTitle("Add a Friend");
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String usr = e.getText().toString();
                if (usr.equals("")) return;
                try {
                    mController.addFriend(usr);
                } catch (IOException e1) {
                    Snackbar.make(getView(), "There was a problem with the network", Snackbar.LENGTH_SHORT);
                } catch (UserNotFoundException e2) {
                    Snackbar.make(getView(), String.format("User %s does not exist", usr), Snackbar.LENGTH_SHORT).show();
                } catch (UserAlreadyAddedException e1) {
                    Snackbar.make(getView(), String.format("User %s is already added!", usr), Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        t.start();

            }
        });
        AlertDialog d = builder.create();
        return d;
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
//        setupFab();


//        setupRecyclerView();

//        setupListView();

    }

    private void setupListView(){
        mListView = (ListView) getActivity().findViewById(R.id.friendsListListView);

//        FriendsList friendsList = new FriendsList();
//        friendsList.addFriend(new User("steve", getContext()));

//        FriendsListListAdapter adapter = new FriendsListListAdapter(getContext(), friendsList);


        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mModel.getFriends());
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) mListView.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
                intent.putExtra("user", Parcels.wrap(user));
                startActivity(intent);

//                Snackbar.make(getView(), user.getUsername(), Snackbar.LENGTH_SHORT).show();

            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final User user = (User) mListView.getItemAtPosition(position);

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mController.removeFriend(user);
                    }
                });
                t.start();

                return true;


            }
        });

    }


//    private void setupRecyclerView() {
//        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.friendsListRecyclerView);
//
//        FriendsList friendsList = new FriendsList();
//
//        friendsList.addFriend(new User("steve", getContext()));
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

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
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
