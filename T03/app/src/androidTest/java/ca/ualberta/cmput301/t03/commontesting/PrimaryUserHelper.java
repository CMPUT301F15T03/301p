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
    public static final String CITY = "Edmonton";
    public static final String EMAIL = "TESTUSER@gualberta.ca";
    public static final String PHONE = "5555550123";
    public static final String USER_ID = "JUNIT_TEST_USER_DO_NOT_USE_THIS_NAME";
    private static String previousUser;

    public static void setup(Context context) throws Exception {
        Configuration configuration = new Configuration(context);
        if (configuration.isApplicationUserNameSet()) {
            previousUser = configuration.getApplicationUserName();
        }
        configuration.clearApplicationUserName();
        configuration.setApplicationUserName(USER_ID);
        User temp = new User(configuration.getApplicationUserName(), context);
        temp.getFriends();
        temp.getInventory();
        UserProfile prof = temp.getProfile();
        prof.setCity(CITY);
        prof.setEmail(EMAIL);
        prof.setPhone(PHONE);
        prof.commitChanges();
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
