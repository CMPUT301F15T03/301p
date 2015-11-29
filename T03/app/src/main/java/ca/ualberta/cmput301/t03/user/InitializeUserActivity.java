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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;

/**
 * View component of the Initialize user workflow. To be entered if no user is found locally.
 */
public class InitializeUserActivity extends AppCompatActivity {

    private String toast;
    private EditText userNameEditText;
    private EditText emailEditText;
    private EditText cityEditText;
    private EditText phoneNumberEditText;
    private Button doneButton;
    private InitializeUserController controller;

    /**
     * set on click listeners and members relating to the view elements. Gets called on acitvity
     * startup.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize_user);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controller = new InitializeUserController(getApplicationContext());

        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        cityEditText = (EditText) findViewById(R.id.cityEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        doneButton = (Button) findViewById(R.id.doneButton);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneButtonClick();
            }
        });
    }

    /**
     * prevent user from exiting activity until they have properly filled in the form.
     */
    @Override
    public void onBackPressed() {
        doneButton.performClick();
    }

    /**
     * Getter for userNameEditText view element.
     *
     * Used in testing only
     *
     * @return view element
     */
    public EditText getUserNameEditText() {
        return userNameEditText;
    }

    /**
     * Gett for emailEditText view element.
     *
     * Used in testing only
     *
     * @return view element
     */
    public EditText getEmailEditText() {
        return emailEditText;
    }

    /**
     * Getter for citEditText view element.
     *
     * Used in testing only
     *
     * @return view element
     */
    public EditText getCityEditText() {
        return cityEditText;
    }

    /**
     * Getter for phoneNumberEditText view element.
     *
     * Used in testing only
     *
     * @return view element
     */
    public EditText getPhoneNumberEditText() {
        return phoneNumberEditText;
    }

    /**
     * Getter for doneButton view element.
     *
     * Used in testing only
     *
     * @return view element
     */
    public Button getDoneButton() {
        return doneButton;
    }

    /**
     * Callback on done button being pressed. does basic validation and passed off info to
     * controller if checks pass.
     */
    public void onDoneButtonClick() {
        // todo : some of this code should be in the controller

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                InitializeUserActivity.this.toast = null;
                if (userNameEditText.getText().toString().isEmpty()) {
                    toastMessage(getString(R.string.noUserNameToast));
                    return;
                }

                try {
                    if (controller.isUserNameTaken(userNameEditText.getText().toString())) {
                        toastMessage(getString(R.string.userNameTakenToast));
                        return;
                    }
                } catch (IOException e) {
                    toastMessage(getString(R.string.problemWithNetworkToast));
                    return;
                } catch (ServiceNotAvailableException e) {
                    toastMessage("You must be online to create a user");
                    return;
                }

                if (cityEditText.getText().toString().isEmpty()) {
                    toastMessage(getString(R.string.noCityToast));
                    return;
                }

                if (controller.isEmailInValid(emailEditText.getText().toString())) {
                    toastMessage(getString(R.string.invalidEmailToast));
                    return;
                }

                controller.initializeUser(userNameEditText.getText().toString(),
                        cityEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        phoneNumberEditText.getText().toString());

                finish();
            }
        });
        thread.start();
    }

    /**
     * Toast a message to the user.
     *
     * @param toastError the error text you want to print
     */
    public void toastMessage(final String toastError) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InitializeUserActivity.this.toast = toastError;
                Toast.makeText(InitializeUserActivity.this, toastError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Get the last toast message that was displayed.
     *
     * @return toast message
     */
    public String getToast() {
        return toast;
    }

}
