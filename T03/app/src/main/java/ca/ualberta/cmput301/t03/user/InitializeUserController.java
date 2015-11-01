package ca.ualberta.cmput301.t03.user;

import android.content.Context;
import android.util.Patterns;

import java.io.IOException;
import java.net.MalformedURLException;

import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.LocalDataManager;
import ca.ualberta.cmput301.t03.datamanager.httpdatamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.Inventory;

/**
 * Created by ross on 15-10-29.
 */
public class InitializeUserController {
    private Configuration configuration;
    private DataManager dataManager;
    private Context context;

    public InitializeUserController(Context context) {
        this.context = context;
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
        configuration.setApplicationUserName(username);
        User localUser = null;
        try {
            localUser = new User(username, context);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue creating new user.");
        }
        UserProfile localUserProfile = null;
        try {
            localUserProfile = localUser.getProfile();
        } catch (IOException e) {
            throw new RuntimeException("Issue getting user's profile.");

        }
        localUserProfile.setCity(city);
        localUserProfile.setEmail(email);
        localUserProfile.setPhone(phoneNumber);

        localUserProfile.commitChanges();

        Inventory localUserInventory = null;
        try {
            localUserInventory = localUser.getInventory();
        } catch (IOException e) {
            throw new RuntimeException("Issue getting user's inventory.");
        }

        FriendsList localUserFriends = null;

        try {
            localUserFriends = localUser.getFriends();
        } catch (IOException e) {
            throw new RuntimeException("Issue getting user's friendsList.");
        }
    }
}
