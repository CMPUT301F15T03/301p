package ca.ualberta.cmput301.t03.commontesting;

import android.content.Context;

import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.CachedDataManager;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.user.FriendsList;
import ca.ualberta.cmput301.t03.user.User;
import ca.ualberta.cmput301.t03.user.UserProfile;

/**
 * Created by kyleoshaughnessy on 2015-11-04.
 */
public class PrimaryUserHelper {
    private static String previousUser;

    public static void setup(Context context) throws Exception {
        Configuration configuration = new Configuration(context);
        if (configuration.isApplicationUserNameSet()) {
            previousUser = configuration.getApplicationUserName();
        }
        configuration.clearApplicationUserName();
        configuration.setApplicationUserName("JUNIT_TEST_USER_DO_NOT_USE_THIS_NAME");
        User temp = new User(configuration.getApplicationUserName(), context);
        temp.getFriends();
        temp.getInventory();
        UserProfile prof = temp.getProfile();
        prof.setCity("Edmonton");
        prof.setEmail("TESTUSER@gualberta.ca");
        prof.setPhone("5555550123");
        prof.commitChanges();
    }

    public static void setupInventoryFriend1(Context context) throws Exception {
        Configuration configuration = new Configuration(context);
        if (configuration.isApplicationUserNameSet()) {
            previousUser = configuration.getApplicationUserName();
        }
        configuration.clearApplicationUserName();
        configuration.setApplicationUserName("GENERAL_INVENTORY_FRIEND_1");
        User temp = new User(configuration.getApplicationUserName(), context);
        temp.getFriends();
        temp.getInventory();
        UserProfile prof = temp.getProfile();
        prof.setCity("Edmonton");
        prof.setEmail("TESTUSER1@gualberta.ca");
        prof.setPhone("5555550123");
        prof.commitChanges();
    }

    public static void tearDownInventoryFriend1(Context context) throws Exception {
        DataManager dataManager = new CachedDataManager(new HttpDataManager(context, true), context, true);
        Configuration configuration = new Configuration(context);
        configuration.setApplicationUserName("GENERAL_INVENTORY_FRIEND_1");
        dataManager.deleteIfExists(new DataKey(UserProfile.type, configuration.getApplicationUserName()));
        dataManager.deleteIfExists(new DataKey(Inventory.type, configuration.getApplicationUserName()));
        dataManager.deleteIfExists(new DataKey(FriendsList.type, configuration.getApplicationUserName()));
        configuration.clearApplicationUserName();
        if (previousUser != null) {
            configuration.setApplicationUserName(previousUser);
        }
        previousUser = null;
    }

    public static void tearDown(Context context) throws Exception {
        DataManager dataManager = new CachedDataManager(new HttpDataManager(context, true), context, true);
        Configuration configuration = new Configuration(context);
        configuration.setApplicationUserName("JUNIT_TEST_USER_DO_NOT_USE_THIS_NAME");
        dataManager.deleteIfExists(new DataKey(UserProfile.type, configuration.getApplicationUserName()));
        dataManager.deleteIfExists(new DataKey(Inventory.type, configuration.getApplicationUserName()));
        dataManager.deleteIfExists(new DataKey(FriendsList.type, configuration.getApplicationUserName()));
        configuration.clearApplicationUserName();
        if (previousUser != null) {
            configuration.setApplicationUserName(previousUser);
        }
        previousUser = null;
    }
}
