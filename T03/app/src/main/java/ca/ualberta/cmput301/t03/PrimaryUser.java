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

package ca.ualberta.cmput301.t03;

import android.content.Context;

import java.io.IOException;

import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.user.User;

/**
 * PrimaryUser is a singleton which holds a reference to the Application's user.
 */
public class PrimaryUser {
    private static User ourInstance = null;
    private static Context context = null;
    private static Configuration config = null;
    private static Boolean setupCalled = false;

    /**
     * Hidden default constructor to enforce calling the static getInstance.
     */
    private PrimaryUser() {
    }

    /**
     * Initializes the singleton with a context which it will user for both a User (dataManager)
     * and Configuration, calling setup prior to getInstance prevents a runtime exception.
     *
     * Called by the main activity to capture the initial startup of the application.
     *
     * @param context the application context
     */
    public static void setup(Context context) {
        PrimaryUser.context = context;
        PrimaryUser.config = new Configuration(context);
        PrimaryUser.setupCalled = true;
    }

    /**
     * Get the User referenced by the singleton, fully initialized with Inventory, FriendsList,
     * and UserProfile.
     *
     * @return the application's single user
     */
    public static User getInstance() {
        if (!setupCalled) {
            throw new RuntimeException("PrimaryUser must be setup(...) first.");
        }
        if (ourInstance == null) {
            try {
                ourInstance = new User(config.getApplicationUserName(), context);
                ourInstance.getInventory();
                ourInstance.getFriends();
                ourInstance.getProfile();
                ourInstance.getTradeList();
            } catch (IOException e) {
                throw new RuntimeException("Issue grabbing User's fields");
            } catch (ServiceNotAvailableException e) {
                throw new RuntimeException("App is offline.", e);
            }
        }
        return ourInstance;
    }

    /**
     * Destroys the current user and detaches all observers.
     *
     * Generally, this should NOT be used in the app!
     * It is only useful for testing purposes, when switching users.
     */
    public static void clearInstance() {
        setupCalled = false;
        if (ourInstance != null){
            ourInstance.clearObservers();
            ourInstance = null;
        }
    }
}
