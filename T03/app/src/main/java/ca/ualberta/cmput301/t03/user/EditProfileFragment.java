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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;


/**
 * View-component to editing a User's profile.
 *
 * User can see and modify all their editable profile fields,
 * and make changes to the profile.
 *
 */
public class EditProfileFragment extends Fragment implements Observer {
    private UserProfile model;
    private UserProfileController controller;
    private User user;
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPhoneField;
    private EditText mCityField;


    /**
     * Use newInstance instead...
     */
    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Instantiates a new instance of the EditProfileFragment with
     * default arguments.
     *
     * @return a new instance of EditProfileFragment
     */
    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = PrimaryUser.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);

    }

    /**
     * Callback after model data has been fetched.
     *
     * Populates the EditText fields with values from the model
     * once the data has been fetched, and sets up an
     * OnFocusChangeListener to update the model for new input.
     *
     */
    public void populateFields() {
        mNameField.setText(user.getUsername());
        mCityField.setText(model.getCity());
        mEmailField.setText(model.getEmail());
        mPhoneField.setText(model.getPhone());

        mEmailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    TextView t = (TextView) v;
                    if (controller.isEmailInValid(t.getText().toString())) {
                        Snackbar.make(getView(), "Invalid email!", Snackbar.LENGTH_SHORT).show();
                        t.setText("");
                    }
                }
            }
        });

        mCityField.addTextChangedListener(controller.getCityWatcher());
        mPhoneField.addTextChangedListener(controller.getPhoneWatcher());
        mEmailField.addTextChangedListener(controller.getEmailWatcher());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNameField = (EditText) getView().findViewById(R.id.profileNameEditText);
        mCityField = (EditText) getView().findViewById(R.id.profileCityEditText);
        mEmailField = (EditText) getView().findViewById(R.id.profileEmailEditText);
        mPhoneField = (EditText) getView().findViewById(R.id.profilePhoneEditText);

        AsyncTask worker = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
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
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
                return null;
            }
        };
        worker.execute();


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        new AsyncTask<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... params) {
                controller.commitChanges();
                return null;
            }
        }.execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Observable observable) {

    }
}
