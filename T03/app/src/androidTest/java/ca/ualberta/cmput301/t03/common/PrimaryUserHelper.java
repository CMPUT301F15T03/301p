/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
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

package ca.ualberta.cmput301.t03.common;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import java.io.IOException;

import ca.ualberta.cmput301.t03.PrimaryUser;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import com.udeyrishi.androidelasticsearchdatamanager.datamanagers.CachedDataManager;
import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.datamanagers.DataManager;
import com.udeyrishi.androidelasticsearchdatamanager.datamanagers.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.trading.TradeList;
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
    public static final String USER_ID = "junitdude";
    public static final String FRIEND_WITH_AN_INVENTORY = "friendwithaninventory";
    public static final String FRIEND_WITH_AN_INVENTORY2 = "friendwithaninventory2";
    public static final String GENERAL_INVENTORY_FRIEND_1 = "generalinventoryfriend1";
    public static final String TEST_ITEM_1_F_2 = "testItem1f2";
    public static final String TEST_QUALITY = "testQuality";
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


    public static void createAndLoadUserWithFriendThatHasInventory(Context context) {

        Configuration configuration = new Configuration(context);
        if (configuration.isApplicationUserNameSet()) {
            previousUser = configuration.getApplicationUserName();
        }
        configuration.clearApplicationUserName();
        configuration.setApplicationUserName(FRIEND_WITH_AN_INVENTORY);

        User friend = new User(FRIEND_WITH_AN_INVENTORY, context);

        try {
            Inventory friendInventory = friend.getInventory();

            Item item1_f1 = new Item("testItem1f1", "testQuality");
            item1_f1.setItemCategory("Cameras");
            item1_f1.setItemIsPrivate(false);
            item1_f1.setItemQuantity(1);
            item1_f1.setItemDescription("Test Description FX10");

            Item item2_f1 = new Item("testItem2f1", "testQuality");
            item2_f1.setItemCategory("Stands");
            item2_f1.setItemIsPrivate(false);
            item2_f1.setItemQuantity(1);
            item2_f1.setItemDescription("Test Description FX9");

            Item item3_f1 = new Item("testItem3f1", "testQuality");
            item3_f1.setItemCategory("Cameras");
            item3_f1.setItemIsPrivate(true);
            item3_f1.setItemQuantity(1);
            item3_f1.setItemDescription("Test Description FX10");

            friendInventory.addItem(item1_f1);
            friendInventory.addItem(item2_f1);
            friendInventory.addItem(item3_f1);

            UserProfile friendProfile = friend.getProfile();
            friendProfile.setCity("Deadmonton");
            friendProfile.setEmail("friend@email.com");
            friendProfile.setPhone("1234567891");
            friendInventory.commitChanges();
            FriendsList friendsfriendList = friend.getFriends();
            friendsfriendList.commitChanges();
            friendProfile.commitChanges();

            configuration.clearApplicationUserName();
            configuration.setApplicationUserName(FRIEND_WITH_AN_INVENTORY2);
            User friend2 = new User(FRIEND_WITH_AN_INVENTORY2, context);


            Inventory friendInventory2 = friend2.getInventory();

            Item item1_f2 = new Item(TEST_ITEM_1_F_2, TEST_QUALITY);
            item1_f2.setItemCategory("Cameras");
            item1_f2.setItemIsPrivate(false);
            item1_f2.setItemQuantity(1);
            item1_f2.setItemDescription("Test Description FX10");

            Item item2_f2 = new Item("testItem2f2", "testQuality");
            item2_f2.setItemCategory("Stands");
            item2_f2.setItemIsPrivate(false);
            item2_f2.setItemQuantity(1);
            item2_f2.setItemDescription("Test Description FX9");

            Item item3_f2 = new Item("testItem3f2", "testQuality");
            item3_f2.setItemCategory("Cameras");
            item3_f2.setItemIsPrivate(true);
            item3_f2.setItemQuantity(1);
            item3_f2.setItemDescription("Test Description FX10");

            friendInventory2.addItem(item1_f2);
            friendInventory2.addItem(item2_f2);
            friendInventory2.addItem(item3_f2);

            UserProfile friendProfile2 = friend2.getProfile();
            friendProfile2.setCity("Deadmonton");
            friendProfile2.setEmail("friend2@email.com");
            friendProfile2.setPhone("1234567892");
            friendInventory2.commitChanges();
            FriendsList friendsfriendList2 = friend2.getFriends();
            friendsfriendList2.commitChanges();
            friendProfile2.commitChanges();

            configuration.clearApplicationUserName();
            configuration.setApplicationUserName(GENERAL_INVENTORY_FRIEND_1);
            User user = new User(GENERAL_INVENTORY_FRIEND_1, context);

            FriendsList friendsList = user.getFriends();
            friendsList.addFriend(friend);
            friendsList.addFriend(friend2);
            friendsList.commitChanges();
            UserProfile userProfile = user.getProfile();
            userProfile.setCity("Deadmonton");
            userProfile.setEmail("user@email.com");
            userProfile.setPhone("1234567891");
            Inventory userInventory = user.getInventory();
            Item item1 = new Item("testItem1", "testQuality");
            item1.setItemCategory("Cameras");
            item1.setItemIsPrivate(false);
            item1.setItemQuantity(1);
            item1.setItemDescription("Test Description FX10");

            Item item2 = new Item("testItem2", "testQuality");
            item2.setItemCategory("Stands");
            item2.setItemIsPrivate(false);
            item2.setItemQuantity(1);
            item2.setItemDescription("Test Description FX9");

            Item item3 = new Item("testItem3", "testQuality");
            item3.setItemCategory("Cameras");
            item3.setItemIsPrivate(true);
            item3.setItemQuantity(1);
            item3.setItemDescription("Test Description FX10");

            userInventory.addItem(item1);
            userInventory.addItem(item2);
            userInventory.addItem(item3);

            userInventory.commitChanges();
            userProfile.commitChanges();
            user.notifyObservers();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceNotAvailableException e) {
            throw new RuntimeException("App is offline.", e);
        }


    }

    public static void deleteAndUnloadUserWithFriendThatHasInventory(Context context) throws Exception {

        deleteUserWithFriend(context);
    }


    public static void createUsers_NoFriendsAdded(Context context) throws Exception {

        Configuration configuration = new Configuration(context);
        if (configuration.isApplicationUserNameSet()) {
            previousUser = configuration.getApplicationUserName();
        }
        configuration.clearApplicationUserName();
        configuration.setApplicationUserName(FRIEND_WITH_AN_INVENTORY);

        User friend = new User(FRIEND_WITH_AN_INVENTORY, context);


        Inventory friendInventory = friend.getInventory();

        Item item1_f1 = new Item("testItem1f1", "testQuality");
        item1_f1.setItemCategory("Cameras");
        item1_f1.setItemIsPrivate(false);
        item1_f1.setItemQuantity(1);
        item1_f1.setItemDescription("Test Description FX10");

        Item item2_f1 = new Item("testItem2f1", "testQuality");
        item2_f1.setItemCategory("Stands");
        item2_f1.setItemIsPrivate(false);
        item2_f1.setItemQuantity(1);
        item2_f1.setItemDescription("Test Description FX9");

        Item item3_f1 = new Item("testItem3f1", "testQuality");
        item3_f1.setItemCategory("Cameras");
        item3_f1.setItemIsPrivate(true);
        item3_f1.setItemQuantity(1);
        item3_f1.setItemDescription("Test Description FX10");

        friendInventory.addItem(item1_f1);
        friendInventory.addItem(item2_f1);
        friendInventory.addItem(item3_f1);

        UserProfile friendProfile = friend.getProfile();
        friendProfile.setCity("Deadmonton");
        friendProfile.setEmail("friend@email.com");
        friendProfile.setPhone("1234567891");
        friendInventory.commitChanges();
        FriendsList friendsfriendList = friend.getFriends();
        friendsfriendList.commitChanges();
        friendProfile.commitChanges();

        configuration.clearApplicationUserName();
        configuration.setApplicationUserName(FRIEND_WITH_AN_INVENTORY2);
        User friend2 = new User(FRIEND_WITH_AN_INVENTORY2, context);


        Inventory friendInventory2 = friend2.getInventory();

        Item item1_f2 = new Item("testItem1f2", "testQuality");
        item1_f2.setItemCategory("Cameras");
        item1_f2.setItemIsPrivate(false);
        item1_f2.setItemQuantity(1);
        item1_f2.setItemDescription("Test Description FX10");

        Item item2_f2 = new Item("testItem2f2", "testQuality");
        item2_f2.setItemCategory("Stands");
        item2_f2.setItemIsPrivate(false);
        item2_f2.setItemQuantity(1);
        item2_f2.setItemDescription("Test Description FX9");

        Item item3_f2 = new Item("testItem3f2", "testQuality");
        item3_f2.setItemCategory("Cameras");
        item3_f2.setItemIsPrivate(true);
        item3_f2.setItemQuantity(1);
        item3_f2.setItemDescription("Test Description FX10");

        friendInventory2.addItem(item1_f2);
        friendInventory2.addItem(item2_f2);
        friendInventory2.addItem(item3_f2);

        UserProfile friendProfile2 = friend2.getProfile();
        friendProfile2.setCity("Deadmonton");
        friendProfile2.setEmail("friend2@email.com");
        friendProfile2.setPhone("1234567892");
        friendInventory2.commitChanges();
        FriendsList friendsfriendList2 = friend2.getFriends();
        friendsfriendList2.commitChanges();
        friendProfile2.commitChanges();

        configuration.clearApplicationUserName();
        configuration.setApplicationUserName(GENERAL_INVENTORY_FRIEND_1);
        User user = new User(GENERAL_INVENTORY_FRIEND_1, context);

        FriendsList friendsList = user.getFriends();
//            friendsList.commitChanges();
        UserProfile userProfile = user.getProfile();
        userProfile.setCity("Deadmonton");
        userProfile.setEmail("user@email.com");
        userProfile.setPhone("1234567891");
        Inventory userInventory = user.getInventory();
        Item item1 = new Item("testItem1", "testQuality");
        item1.setItemCategory("Cameras");
        item1.setItemIsPrivate(false);
        item1.setItemQuantity(1);
        item1.setItemDescription("Test Description FX10");

        Item item2 = new Item("testItem2", "testQuality");
        item2.setItemCategory("Stands");
        item2.setItemIsPrivate(false);
        item2.setItemQuantity(1);
        item2.setItemDescription("Test Description FX9");

        Item item3 = new Item("testItem3", "testQuality");
        item3.setItemCategory("Cameras");
        item3.setItemIsPrivate(true);
        item3.setItemQuantity(1);
        item3.setItemDescription("Test Description FX10");

        userInventory.addItem(item1);
        userInventory.addItem(item2);
        userInventory.addItem(item3);

        userInventory.commitChanges();
        userProfile.commitChanges();



    }

    public static void createUsers_WithFriendsAdded(Context context) throws Exception {

        Configuration configuration = new Configuration(context);
        if (configuration.isApplicationUserNameSet()) {
            previousUser = configuration.getApplicationUserName();
        }
        configuration.clearApplicationUserName();
        configuration.setApplicationUserName(FRIEND_WITH_AN_INVENTORY);

        User friend = new User(FRIEND_WITH_AN_INVENTORY, context);


        Inventory friendInventory = friend.getInventory();

        Item item1_f1 = new Item("testItem1f1", "testQuality");
        item1_f1.setItemCategory("Cameras");
        item1_f1.setItemIsPrivate(false);
        item1_f1.setItemQuantity(1);
        item1_f1.setItemDescription("Test Description FX10");

        Item item2_f1 = new Item("testItem2f1", "testQuality");
        item2_f1.setItemCategory("Stands");
        item2_f1.setItemIsPrivate(false);
        item2_f1.setItemQuantity(1);
        item2_f1.setItemDescription("Test Description FX9");

        Item item3_f1 = new Item("testItem3f1", "testQuality");
        item3_f1.setItemCategory("Cameras");
        item3_f1.setItemIsPrivate(true);
        item3_f1.setItemQuantity(1);
        item3_f1.setItemDescription("Test Description FX10");

        friendInventory.addItem(item1_f1);
        friendInventory.addItem(item2_f1);
        friendInventory.addItem(item3_f1);

        UserProfile friendProfile = friend.getProfile();
        friendProfile.setCity("Deadmonton");
        friendProfile.setEmail("friend@email.com");
        friendProfile.setPhone("1234567891");
        friendInventory.commitChanges();
        FriendsList friendsfriendList = friend.getFriends();
        friendsfriendList.commitChanges();
        friendProfile.commitChanges();

        configuration.clearApplicationUserName();
        configuration.setApplicationUserName(FRIEND_WITH_AN_INVENTORY2);
        User friend2 = new User(FRIEND_WITH_AN_INVENTORY2, context);


        Inventory friendInventory2 = friend2.getInventory();

        Item item1_f2 = new Item("testItem1f2", "testQuality");
        item1_f2.setItemCategory("Cameras");
        item1_f2.setItemIsPrivate(false);
        item1_f2.setItemQuantity(1);
        item1_f2.setItemDescription("Test Description FX10");

        Item item2_f2 = new Item("testItem2f2", "testQuality");
        item2_f2.setItemCategory("Stands");
        item2_f2.setItemIsPrivate(false);
        item2_f2.setItemQuantity(1);
        item2_f2.setItemDescription("Test Description FX9");

        Item item3_f2 = new Item("testItem3f2", "testQuality");
        item3_f2.setItemCategory("Cameras");
        item3_f2.setItemIsPrivate(true);
        item3_f2.setItemQuantity(1);
        item3_f2.setItemDescription("Test Description FX10");

        friendInventory2.addItem(item1_f2);
        friendInventory2.addItem(item2_f2);
        friendInventory2.addItem(item3_f2);

        UserProfile friendProfile2 = friend2.getProfile();
        friendProfile2.setCity("Deadmonton");
        friendProfile2.setEmail("friend2@email.com");
        friendProfile2.setPhone("1234567892");
        friendInventory2.commitChanges();
        FriendsList friendsfriendList2 = friend2.getFriends();
        friendsfriendList2.commitChanges();
        friendProfile2.commitChanges();

        configuration.clearApplicationUserName();
        configuration.setApplicationUserName(GENERAL_INVENTORY_FRIEND_1);
        User user = new User(GENERAL_INVENTORY_FRIEND_1, context);

        FriendsList friendsList = user.getFriends();
        friendsList.addFriend(friend);
        friendsList.addFriend(friend2);
        friendsList.commitChanges();
        UserProfile userProfile = user.getProfile();
        userProfile.setCity("Deadmonton");
        userProfile.setEmail("user@email.com");
        userProfile.setPhone("1234567891");
        Inventory userInventory = user.getInventory();
        Item item1 = new Item("testItem1", "testQuality");
        item1.setItemCategory("Cameras");
        item1.setItemIsPrivate(false);
        item1.setItemQuantity(1);
        item1.setItemDescription("Test Description FX10");

        Item item2 = new Item("testItem2", "testQuality");
        item2.setItemCategory("Stands");
        item2.setItemIsPrivate(false);
        item2.setItemQuantity(1);
        item2.setItemDescription("Test Description FX9");

        Item item3 = new Item("testItem3", "testQuality");
        item3.setItemCategory("Cameras");
        item3.setItemIsPrivate(true);
        item3.setItemQuantity(1);
        item3.setItemDescription("Test Description FX10");

        userInventory.addItem(item1);
        userInventory.addItem(item2);
        userInventory.addItem(item3);

        userInventory.commitChanges();
        userProfile.commitChanges();



    }

    public static void deleteUserWithFriend(Context context) throws Exception {

        DataManager dataManager = new CachedDataManager(context, new HttpDataManager(context, context.getString(R.string.elasticSearchRootUrl), true));
        Configuration configuration = new Configuration(context);

        String users[] = {GENERAL_INVENTORY_FRIEND_1, FRIEND_WITH_AN_INVENTORY, FRIEND_WITH_AN_INVENTORY2};

        for (String user: users){
            configuration.setApplicationUserName(user);

            dataManager.deleteIfExists(new DataKey(UserProfile.type, configuration.getApplicationUserName()));
            dataManager.deleteIfExists(new DataKey(Inventory.type, configuration.getApplicationUserName()));
            dataManager.deleteIfExists(new DataKey(FriendsList.type, configuration.getApplicationUserName()));
            dataManager.deleteIfExists(new DataKey(TradeList.type, configuration.getApplicationUserName()));

        }

        configuration.clearApplicationUserName();
        if (previousUser != null){
            configuration.setApplicationUserName(previousUser);
        }
        previousUser = null;
        PrimaryUser.clearInstance();
    }


    public static void tearDown(Context context) throws Exception {
        DataManager dataManager = new CachedDataManager(context, new HttpDataManager(context, context.getString(R.string.elasticSearchRootUrl), true));
        Configuration configuration = new Configuration(context);
        configuration.setApplicationUserName(USER_ID);
        for (Item item : PrimaryUser.getInstance().getInventory().getItems().values()) {
            item.clearPhotoList();
        }

        dataManager.deleteIfExists(new DataKey(UserProfile.type, configuration.getApplicationUserName()));
        dataManager.deleteIfExists(new DataKey(Inventory.type, configuration.getApplicationUserName()));
        dataManager.deleteIfExists(new DataKey(FriendsList.type, configuration.getApplicationUserName()));
        dataManager.deleteIfExists(new DataKey(TradeList.type, configuration.getApplicationUserName()));
        configuration.clearApplicationUserName();
        if (previousUser != null) {
            configuration.setApplicationUserName(previousUser);
        }
        previousUser = null;
        PrimaryUser.clearInstance();
    }
}
