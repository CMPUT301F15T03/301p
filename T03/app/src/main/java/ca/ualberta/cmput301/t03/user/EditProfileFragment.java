/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment implements Observer {
    private UserProfile model;
    private UserProfileController controller;
    private User user;
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPhoneField;
    private EditText mCityField;


    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration c = new Configuration(getContext());
        c.getApplicationUserName();

        user = new User(c.getApplicationUserName(), getContext());

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    model = user.getProfile();
                    controller = new UserProfileController(model);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateFields();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);

    }

    public void populateFields(){
        mNameField.setText(user.getUsername());
        mCityField.setText(model.getCity());
        mEmailField.setText(model.getEmail());
        mPhoneField.setText(model.getPhone());


        mCityField.addTextChangedListener(controller.getCityWatcher());
        mPhoneField.addTextChangedListener(controller.getPhoneWatcher());
        mEmailField.addTextChangedListener(controller.getEmailWatcher());
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNameField = (EditText) getActivity().findViewById(R.id.profileNameEditText);
        mCityField = (EditText) getActivity().findViewById(R.id.profileCityEditText);
        mEmailField = (EditText) getActivity().findViewById(R.id.profileEmailEditText);
        mPhoneField = (EditText) getActivity().findViewById(R.id.profilePhoneEditText);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        new AsyncTask<Object, Object, Object>(){

            @Override
            protected Object doInBackground(Object... params) {
                controller.commitChanges();
                return null;
            }
        }.execute();

    }

    @Override
    public void update(Observable observable) {

    }
}
