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
import java.net.MalformedURLException;

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



    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration c = new Configuration(getContext());
        c.getApplicationUserName();

        user = null;
        try {
            user = new User(c.getApplicationUserName(), getContext());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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
