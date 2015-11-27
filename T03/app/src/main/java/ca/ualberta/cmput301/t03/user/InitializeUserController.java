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

import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.Inventory;

/**
 * Controller for the InitializeUser Activity. Responsible for validating input of the view's form.
 * Will be used to create a fresh user locally and on ElasticSearch.
 */
public class InitializeUserController {
    private Configuration configuration;
    private DataManager dataManager;
    private Context context;

    /**
     * Constructor for InitializeUserController. Called by the initialize user activity.
     *
     * @param context application context
     */
    public InitializeUserController(Context context) {
        this.context = context;
        this.configuration = new Configuration(context);
        this.dataManager = new HttpDataManager();
    }

    /**
     * Check's both elasticSearch and the local cache to determine if username is taken.
     *
     * @param username username that we want to know if has been taken
     * @return true == taken, false == not taken
     * @throws IOException
     */
    public boolean isUserNameTaken(String username) throws IOException, ServiceNotAvailableException {
        return dataManager.keyExists(new DataKey(UserProfile.type, username.toLowerCase()));
    }

    /**
     * Check to see if an email matches the valid syntax.
     *
     * @param email email that will be validated
     * @return true == valid, false == invalid
     */
    public boolean isEmailInValid(String email) {
        return email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Setup user, profile, inventory, and friendslist entities locally and on ElasticSearch.
     *
     * @param username    username of the new user
     * @param city        city to be entered in user profile
     * @param email       email to be entered in the user profile
     * @param phoneNumber phone number to be entered in user profile (can be empty)
     */
    public void initializeUser(String username, String city, String email, String phoneNumber) {
        configuration.setApplicationUserName(username);

        // create user
        User localUser = new User(username.toLowerCase(), context);

        // get userProfile -> sets up ES
        UserProfile localUserProfile = null;
        try {
            localUserProfile = localUser.getProfile();
        } catch (IOException e) {
            throw new RuntimeException("Issue getting user's profile.");

        } catch (ServiceNotAvailableException e) {
            throw new RuntimeException("App is offline.", e);
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
        } catch (ServiceNotAvailableException e) {
            throw new RuntimeException("App is offline.", e);
        }

        // get userFriendsList -> this sets up ES
        FriendsList localUserFriends = null;
        try {
            localUserFriends = localUser.getFriends();
        } catch (IOException e) {
            throw new RuntimeException("Issue getting user's friendsList.");
        } catch (ServiceNotAvailableException e) {
            throw new RuntimeException("App is offline.", e);
        }
    }
}
