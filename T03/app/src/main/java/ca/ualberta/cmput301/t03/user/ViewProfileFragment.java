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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.parceler.Parcels;

import java.io.IOException;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;


/**
 * Fragment which provides an interface for viewing a {@link User}'s {@link UserProfile}.
 */
public class ViewProfileFragment extends Fragment implements Observer {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";

    private UserProfile model;
    private TextView cityView;
    private TextView phoneView;
    private TextView usernameView;
    private TextView emailView;
    private Button browseInventoryButton;
    private User mUserToView;

    /**
     * DO NOT CALL THIS CONSTRUCTOR DIRECTLY!
     *
     * Use newInstance instead.
     */
    public ViewProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * ex.
     *  User u = new User("john12345", getContext());
     *  ViewProfileFragment v = ViewProfileFragment.newInstance(u);
     *  getSupportFragmentManager()
     *         .beginTransaction()
     *         .add(R.id.my_content_view, v)
     *         .commit()
     *
     * @param user The User whose profile should be viewed.
     * @return A new instance of fragment ViewProfileFragment.
     */
    public static ViewProfileFragment newInstance(User user) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, Parcels.wrap(user));

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (PrimaryUser.getInstance().equals(mUserToView)) {
            inflater.inflate(R.menu.fragment_view_profile, menu);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
//            String username = getArguments().getString(ARG_PARAM1);
            mUserToView = Parcels.unwrap(getArguments().getParcelable(ARG_PARAM1));
//            mUserToView = new User(username, getActivity().getApplicationContext());
        } else {
            mUserToView = PrimaryUser.getInstance();
        }
        setHasOptionsMenu(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

    /**
     * To be called when the "browse inventory" button is selected (from the view).
     *
     * Starts a new ViewInventoryActivity to browse mUserToView's inventory.
     *
     * @param v view from OnClickListener.onClick
     */
    void onBrowseInventoryButtonSelected(View v) {
        Intent intent = new Intent(v.getContext(), ViewInventoryActivity.class);
        intent.putExtra("user", Parcels.wrap(mUserToView));
        startActivity(intent);
    }

    /**
     * Callback after the model is loaded. (from View)
     *
     * RUN ME on the UI Thread!
     *
     * Populates the view Text fields, as well as sets up the
     * Browse Inventory button to view the correct user's inventory.
     */
    void populateFields() {
        browseInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBrowseInventoryButtonSelected(v);
            }
        });
        model.addObserver(this);
        update(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        usernameView = (TextView) getView().findViewById(R.id.viewProfileUsername);
        emailView = (TextView) getView().findViewById(R.id.viewProfileEmail);
        phoneView = (TextView) getView().findViewById(R.id.viewProfilePhone);
        cityView = (TextView) getView().findViewById(R.id.viewProfileCity);
        browseInventoryButton = (Button) getView().findViewById(R.id.inventoryButton);

        AsyncTask<Void, Integer, UserProfile> t = new AsyncTask<Void, Integer, UserProfile>() {
            @Override
            protected UserProfile doInBackground(Void[] params) {
                //TODO is there a better way to do this?
                if (!PrimaryUser.getInstance().equals(mUserToView)) {
                    mUserToView = new User(mUserToView.getUsername(), getContext());
                } else {
                    mUserToView = PrimaryUser.getInstance();
                }

                try {
                    model = mUserToView.getProfile();
                } catch (IOException e) {
                    //TODO this is garbage.
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
                return model;
            }

            @Override
            protected void onPostExecute(UserProfile userProfile) {
                populateFields();
            }
        };
        t.execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile_button:
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetach() {
        if (model != null){
            model.removeObserver(this);
        }
        super.onDetach();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Observable observable) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String city = model.getCity();
                String email = model.getEmail();
                String phone = model.getPhone();
                String username = mUserToView.getUsername();

                cityView.setText(city);
                emailView.setText(email);
                phoneView.setText(phone);
                usernameView.setText(username);
            }
        });

    }
}
