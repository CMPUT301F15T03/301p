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

package ca.ualberta.cmput301.t03.commontesting;

import android.content.Context;

import java.io.IOException;

import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.CachedDataManager;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.inventory.Item;
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


    public static void createAndLoadUserWithFriendThatHasInventory(Context context){
        String GENERAL_INVENTORY_FRIEND_1 = "GENERAL_INVENTORY_FRIEND_1";
        String FRIEND_WITH_AN_INVENTORY = "FRIEND_WITH_AN_INVENTORY";

        Configuration configuration = new Configuration(context);
        if (configuration.isApplicationUserNameSet()) {
            previousUser = configuration.getApplicationUserName();
        }
        configuration.clearApplicationUserName();

        User friend = new User("FRIEND_WITH_AN_INVENTORY", context);
        User user = new User(GENERAL_INVENTORY_FRIEND_1, context);
        try {
            Inventory friendInventory = friend.getInventory();
            friendInventory.addItem(new Item("TestItem1", "TestItemQuality"));
            UserProfile friendProfile = friend.getProfile();
            friendProfile.setCity("Deadmonton");
            friendProfile.setEmail("friend@email.com");
            friendProfile.setPhone("1234567891");
            friendProfile.commitChanges();
            friendInventory.commitChanges();
            FriendsList friendsfriendList = friend.getFriends();
            friendsfriendList.commitChanges();

            FriendsList friendsList = user.getFriends();
            friendsList.addFriend(friend);
            UserProfile userProfile = user.getProfile();
            userProfile.setCity("Deadmonton");
            userProfile.setEmail("friend@email.com");
            userProfile.setPhone("1234567891");
            userProfile.commitChanges();
            friendsList.commitChanges();
            Inventory userInventory = user.getInventory();
            userInventory.commitChanges();
        } catch (IOException e){
            e.printStackTrace();
        }

        configuration.setApplicationUserName(GENERAL_INVENTORY_FRIEND_1);
    }

    public static void deleteAndUnloadUserWithFriendThatHasInventory(Context context){
        String GENERAL_INVENTORY_FRIEND_1 = "GENERAL_INVENTORY_FRIEND_1";
        String FRIEND_WITH_AN_INVENTORY = "FRIEND_WITH_AN_INVENTORY";

        DataManager dataManager = new CachedDataManager(new HttpDataManager(context, true), context, true);
        Configuration configuration = new Configuration(context);
        configuration.setApplicationUserName(GENERAL_INVENTORY_FRIEND_1);
        try {
            dataManager.deleteIfExists(new DataKey(UserProfile.type, GENERAL_INVENTORY_FRIEND_1));
            dataManager.deleteIfExists(new DataKey(Inventory.type, GENERAL_INVENTORY_FRIEND_1));
            dataManager.deleteIfExists(new DataKey(FriendsList.type, GENERAL_INVENTORY_FRIEND_1));

            dataManager.deleteIfExists(new DataKey(UserProfile.type, FRIEND_WITH_AN_INVENTORY));
            dataManager.deleteIfExists(new DataKey(Inventory.type, FRIEND_WITH_AN_INVENTORY));
            dataManager.deleteIfExists(new DataKey(FriendsList.type, FRIEND_WITH_AN_INVENTORY));
        }catch (IOException e){
            e.printStackTrace();
        }

//        configuration.clearApplicationUserName();
//        if (previousUser != null) {
//            configuration.setApplicationUserName(previousUser);
//        }
//        previousUser = null;
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
