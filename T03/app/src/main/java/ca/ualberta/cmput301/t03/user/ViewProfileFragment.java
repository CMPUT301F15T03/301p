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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.io.IOException;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProfileFragment extends Fragment implements Observer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";
    private UserProfile model;
    private TextView cityView;
    private TextView phoneView;
    private TextView usernameView;
    private TextView emailView;
    // TODO: Rename and change types of parameters
    private User mUserToView;

    private OnFragmentInteractionListener mListener;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Parameter 1.
     * @return A new instance of fragment ViewProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewProfileFragment newInstance(User user) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, username);
        args.putParcelable(ARG_PARAM1, Parcels.wrap(user));


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(PrimaryUser.getInstance().equals(mUserToView)) {
            inflater.inflate(R.menu.fragment_view_profile, menu);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Configuration c = new Configuration(getContext());



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


    void populateFields(){
        String city = model.getCity();
        String email = model.getEmail();
        String phone = model.getPhone();
        String username = mUserToView.getUsername();

        cityView.setText(city);
        emailView.setText(email);
        phoneView.setText(phone);
        usernameView.setText(username);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        usernameView = (TextView) getView().findViewById(R.id.viewProfileUsername);
        emailView = (TextView) getView().findViewById(R.id.viewProfileEmail);
        phoneView = (TextView) getView().findViewById(R.id.viewProfilePhone);
        cityView = (TextView) getView().findViewById(R.id.viewProfileCity);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO is there a better way to do this?
                mUserToView = new User(mUserToView.getUsername(), getContext());

                try {
                    model = mUserToView.getProfile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populateFields();
                    }
                });
            }
        });
        t.start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);

        Snackbar.make(getView(), item.getTitle(), Snackbar.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.edit_profile_button:
                Snackbar.make(getView(), "TODO open edit profile activity",Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);

                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
