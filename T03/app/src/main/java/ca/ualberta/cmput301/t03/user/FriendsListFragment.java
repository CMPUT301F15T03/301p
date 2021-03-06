/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import org.parceler.Parcels;

import java.io.IOException;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;


/**
 * Fragment that displays the User's FriendsList
 * in a ListView.
 * <p>
 * The User can also add Friends here, by
 * pressing the FloatingActionButton.
 */
public class FriendsListFragment extends Fragment implements Observer, SwipeRefreshLayout.OnRefreshListener {
    private FriendsList mModel;
    private FriendsListController mController;
    private RecyclerView mRecyclerView;
    private User mUser;

    private FloatingActionButton addFriendFab;
    private ListView mListView;
    private ArrayAdapter<User> mAdapter;

    private Activity mActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * DO NOT CALL THIS CONSTRUCTOR DIRECTLY
     *
     * instead, use newInstance.
     */
    public FriendsListFragment() {
        // Required empty public constructor
    }

    /**
     * Intantiates a new instance of this fragment with the default arguments
     * set.
     *
     * Usage: Within an activity, call this method to instantiate an instance of
     * this fragment. Then, use the FragmentManager to insert the gragment
     * at the appropriate place.
     *
     * @return a new instance of FriendsListFragment.
     */
    public static FriendsListFragment newInstance() {
        FriendsListFragment fragment = new FriendsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_friends_list, menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mUser = PrimaryUser.getInstance();
    }



    /**
     * Callback method after data has been loaded.
     * <p>
     * Populates appropriate fields, and sets up
     * observers.
     */
    public void populateFields() {
        //do the listview here.
        setupFab();
        setupListView();
        mModel.addObserver(this);


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mModel != null) mModel.removeObserver(this);
    }

    /**
     * Helper method to set up the floatingActionButton.
     * <p>
     * In this case, the button is used initiate an "add friend"
     * action.
     */
    private void setupFab() {
        addFriendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddFriendDialog().show();
            }
        });
        addFriendFab.setImageResource(R.drawable.ic_add_white_24dp);
    }

    /**
     * Creates a new AlertDialog which can be
     * used to add friends.
     * <p>
     * The alertDialog contains a single EditText
     * where the user can enter the username to be
     * added.
     *
     * @return the configured AlertDialog
     */
    private AlertDialog createAddFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogContent = View.inflate(getContext(), R.layout.content_add_friend_dialog, null);
        final EditText e = (EditText) dialogContent.findViewById(R.id.addFriendEditText);

        builder.setView(dialogContent); //todo replace with layout
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String usr = e.getText().toString().trim();

                AsyncTask t = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {

                        if (usr.equals("")) return null;
                        try {
                            mController.addFriend(usr);
                        } catch (IOException e1) {
                            Snackbar.make(getView(), "There was a problem with the network", Snackbar.LENGTH_SHORT);
                        } catch (UserNotFoundException e2) {
                            Snackbar.make(getView(), String.format("User %s does not exist", usr), Snackbar.LENGTH_SHORT).show();
                        } catch (UserAlreadyAddedException e1) {
                            Snackbar.make(getView(), String.format("User %s is already added!", usr), Snackbar.LENGTH_SHORT).show();
                        } catch (ServiceNotAvailableException e1) {
                            Snackbar.make(getView(), "You must be online to add friends!", Snackbar.LENGTH_SHORT).show();
                        }
                        return null;
                    }
                };
                t.execute();
            }
        });
        builder.setTitle("Add a Friend");
        AlertDialog d = builder.create();
        return d;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends_list, container, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addFriendFab = (FloatingActionButton) getActivity().findViewById(R.id.addFriendFab);
        mListView = (ListView) getActivity().findViewById(R.id.friendsListListView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.friendsListSwipeLayout);


        AsyncTask worker = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    mModel = mUser.getFriends();
                    mController = new FriendsListController(getContext(), mModel);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                populateFields();
            }
        };
        worker.execute();
    }

    /**
     * Helper method to set up the friendslist listview.
     * <p>
     * Should only be called after model and controller
     * have been initialized.
     */
    private void setupListView() {
        if(getContext() == null){
            //probably we got killed, so just return
            return;
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mModel.getFriends());
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) mListView.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
                intent.putExtra("user", Parcels.wrap(user));
                startActivity(intent);

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Observable observable) {

        Activity activity = getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRefresh() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    PrimaryUser.getInstance().refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
        task.execute();
    }
}
