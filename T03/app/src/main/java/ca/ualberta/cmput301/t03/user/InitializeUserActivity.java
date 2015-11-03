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

public class InitializeUserActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private EditText emailEditText;
    private EditText cityEditText;
    private EditText phoneNumberEditText;
    private Button doneButton;
    private InitializeUserController controller;

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

    @Override
    public void onBackPressed() {
        doneButton.performClick();
    }

    public EditText getUserNameEditText() {
        return userNameEditText;
    }

    public EditText getEmailEditText() {
        return emailEditText;
    }

    public EditText getCityEditText() {
        return cityEditText;
    }

    public EditText getPhoneNumberEditText() {
        return phoneNumberEditText;
    }

    public Button getDoneButton() {
        return doneButton;
    }

    public void onDoneButtonClick() {
        // todo : some of this code should be in the controller

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String toastError = null;
                if (userNameEditText.getText().toString().isEmpty()) {
                    toastMessage("Please provide a username");
                    return;
                }

                try {
                    if (controller.isUserNameTaken(userNameEditText.getText().toString())) {
                        toastMessage("That username is taken, try again");
                        return;
                    }
                } catch (IOException e) {
                    toastMessage("Error with network, try again later.");
                    return;
                }

                if (cityEditText.getText().toString().isEmpty()) {
                    toastMessage("Please provide a city");
                    return;
                }

                if (controller.isEmailInValid(emailEditText.getText().toString())) {
                    toastMessage("Please provide a valid email address");
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

    public void toastMessage(final String toastError) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(InitializeUserActivity.this, toastError, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
