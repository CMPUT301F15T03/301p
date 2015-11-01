package ca.ualberta.cmput301.t03.user;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import java.io.IOException;
import java.net.MalformedURLException;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.LocalDataManager;
import ca.ualberta.cmput301.t03.datamanager.httpdatamanager.HttpDataManager;

/**
 * Created by ross on 15-10-29.
 */
public class InitializeUserController {
    private Configuration configuration;
    private DataManager dataManager;

    public InitializeUserController(Context context) {
        this.configuration = new Configuration(context);
        try {
            this.dataManager = new HttpDataManager(context);
        } catch (MalformedURLException e) {
            throw new RuntimeException("There has been a issue contacting the application server.");
        }
    }

    public boolean isUserNameTaken(String username) throws IOException {
        return dataManager.keyExists(new DataKey("UserProfile", username));
    }

    public boolean isEmailInValid(String email) {
        return email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void initializeUser(String username, String city, String email, String phoneNumber) {
        configuration.setApplicationUserID(username);
        User localUser = new User(username);
        UserProfile localUserProfile = localUser.getProfile();
        localUserProfile.setCity(city);
        localUserProfile.setEmail(email);
        localUserProfile.setPhone(phoneNumber);
    }
}
