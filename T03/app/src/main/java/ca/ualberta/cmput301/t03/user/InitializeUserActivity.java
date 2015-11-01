package ca.ualberta.cmput301.t03.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
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
    private String toastError;

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
        toastError = null;

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            onDoneButtonClick();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        InitializeUserActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                if (toastError != null) {
                                    Toast.makeText(InitializeUserActivity.this, toastError, Toast.LENGTH_SHORT).show();
                                    toastError = null;
                                }
                                else {
                                    finish();
                                }
                            }
                        });
                    }
                };
                task.execute();
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

    public void onDoneButtonClick() throws IOException {
        if (userNameEditText.getText().toString().isEmpty()) {
            toastError = "Please provide a username";
            return;
        }

        if (controller.isUserNameTaken(userNameEditText.getText().toString())) {
            toastError = "That username is taken, try again";
            return;
        }

        if (cityEditText.getText().toString().isEmpty()) {
            toastError = "Please provide a city";
            return;
        }

        if (controller.isEmailInValid(emailEditText.getText().toString())) {
            toastError = "Please provide a valid email address";
            return;
        }

        controller.initializeUser(userNameEditText.getText().toString(),
                cityEditText.getText().toString(),
                emailEditText.getText().toString(),
                phoneNumberEditText.getText().toString());

    }
}
