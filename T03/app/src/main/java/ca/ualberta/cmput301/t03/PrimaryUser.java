package ca.ualberta.cmput301.t03;

import android.content.Context;

import java.io.IOException;

import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.user.User;

/**
 * Created by kyle on 15-11-03.
 */
public class PrimaryUser {
    private static User ourInstance = null;
    private static Context context = null;
    private static Configuration config = null;
    private static Boolean setupCalled = false;

    private PrimaryUser() {
    }

    public static void setup(Context context) {
        PrimaryUser.context = context;
        PrimaryUser.config = new Configuration(context);
        PrimaryUser.setupCalled = true;
    }

    public static User getInstance() {
        if (!setupCalled) {
            throw new RuntimeException("PrimaryUser must be setup() first.");
        }
        if (ourInstance == null) {
            try {
                ourInstance = new User(config.getApplicationUserName(), context);
                ourInstance.getInventory();
                ourInstance.getFriends();
                ourInstance.getProfile();
            } catch (IOException e) {
                throw new RuntimeException("Issue grabbing User's fields");
            }
        }
        return ourInstance;
    }
}
