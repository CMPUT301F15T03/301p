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

package ca.ualberta.cmput301.t03.user;

import android.content.Context;
import android.util.Patterns;

import java.io.IOException;

import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
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
        User localUser = new User(username, context);

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
//        localUserFriends.addFriend(new User("TestUserKyle22", context));
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
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        assert tempUser != null;
    }
}
