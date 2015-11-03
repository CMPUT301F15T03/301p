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
import ca.ualberta.cmput301.t03.inventory.Item;

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
        this.dataManager = new HttpDataManager(context);
    }

    public boolean isUserNameTaken(String username) throws IOException {
        return dataManager.keyExists(new DataKey(UserProfile.type, username));
    }

    public boolean isEmailInValid(String email) {
        return email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void initializeUser(String username, String city, String email, String phoneNumber) {
        configuration.setApplicationUserName(username);

        // create user
        User localUser = null;
        try {
            localUser = new User(username, context);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue creating new user.");
        }

        // get userProfile -> sets up ES
        UserProfile localUserProfile = null;
        try {
            localUserProfile = localUser.getProfile();
        } catch (IOException e) {
            throw new RuntimeException("Issue getting user's profile.");

        }

        // set profile info from the view
        localUserProfile.setCity(city);
        localUserProfile.setEmail(email);
        localUserProfile.setPhone(phoneNumber);

        // push changes to the user model, this propagates changes to ES
        localUserProfile.commitChanges();

        // get userInventory -> this sets up ES
        Inventory localUserInventory = null;
        try {
            localUserInventory = localUser.getInventory();
        } catch (IOException e) {
            throw new RuntimeException("Issue getting user's inventory.");
        }

        // get userFriendsList -> this sets up ES
        FriendsList localUserFriends = null;
        try {
            localUserFriends = localUser.getFriends();
        } catch (IOException e) {
            throw new RuntimeException("Issue getting user's friendsList.");
        }


//        // test data - add a friend
//        try {
//            localUserFriends.addFriend(new User("TestUserKyle22", context));
//        } catch (MalformedURLException e) {
//        }
//        localUserFriends.commitChanges();
//
//        // test data - add an item
//        Item testItem = new Item();
//        testItem.setItemCategory("lenses");
//        testItem.setItemDescription("Cool lense");
//        testItem.setItemIsPrivate(false);
//        testItem.setItemName("Cannon 50mm lense");
//        testItem.setItemQuality("Good");
//        testItem.setItemQuantity(5);
//        localUserInventory.addItem(testItem);
//        localUserInventory.commitChanges();
//
//        // test data get a user
//        User tempUser = null;
//        try {
//            tempUser = new User("TestUserKyle25", context);
//            tempUser.getProfile();
//            tempUser.getFriends();
//            tempUser.getInventory();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        assert tempUser != null;
    }
}
