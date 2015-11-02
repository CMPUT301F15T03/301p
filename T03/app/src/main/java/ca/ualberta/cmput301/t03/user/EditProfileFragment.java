package ca.ualberta.cmput301.t03.user;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
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
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        worker.start();
        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        controller = new UserProfileController(model);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNameField = (EditText) getActivity().findViewById(R.id.profileNameEditText);
        mCityField = (EditText) getActivity().findViewById(R.id.profileCityEditText);
        mEmailField = (EditText) getActivity().findViewById(R.id.profileEmailEditText);
        mPhoneField = (EditText) getActivity().findViewById(R.id.profilePhoneEditText);

        mNameField.setText(user.getUsername());
        mCityField.setText(model.getCity());
        mEmailField.setText(model.getEmail());
        mPhoneField.setText(model.getPhone());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        controller.setCity(mCityField.toString());
        controller.setEmail(mEmailField.toString());
        controller.setPhone(mPhoneField.toString());

    }

    @Override
    public void update(Observable observable) {

    }
}
