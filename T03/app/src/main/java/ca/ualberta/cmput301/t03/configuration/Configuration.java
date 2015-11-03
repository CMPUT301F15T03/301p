/*
    Copyright (C) 2015 Kyle O'Shaughnessy
    Photography equipment trading application for CMPUT 301 at the University of Alberta.


    This file is part of {Application Name}.


    {Application Name} is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.ualberta.cmput301.t03.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.user.User;

/**
 * Model component of the Configuration MVC triplet.
 * This model will handle maintenance of the application wide settings which others can call upon.
 */
public class Configuration implements Observable {

    private static final String downloadImagesKey = "DOWNLOAD_IMAGES_ENABLED";
    private static final String applicationUserName = "APPLICATION_USER_NAME";

    private  Context context;
    private Set<Observer> observers;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     *
     * @param context Application context provided by the caller to allow for persistent storage
     */
    public Configuration(Context context) {
        this.observers = new HashSet<>();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = preferences.edit();
        this.context = context;
        preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(downloadImagesKey) || key.equals(applicationUserName)) {
                    notifyObservers();
                }
            }
        });
    }

    /**
     * Check if download images is enabled for the application
     * @return Boolean representing if download images setting is enabled or not (true == enabled)
     */
    public Boolean isDownloadImagesEnabled() {
        return preferences.getBoolean(downloadImagesKey, false);
    }

    /**
     * Set the value of download images setting
     * @param state true == enabled, false == disable
     */
    public void setDownloadImages(Boolean state) {
        editor.putBoolean(downloadImagesKey, state);
        editor.commit();
    }

    /**
     * Check to see if a user's id has been assigned to this application
     * @return true == user id has been assigned, false == otherwise
     */
    public Boolean isApplicationUserNameSet() {
        return preferences.contains(applicationUserName);
    }

    /**
     * Get the current user id associated with this application
     * @return the application's associated user id
     */
    public String getApplicationUserName() {
        if (isApplicationUserNameSet()) {
            return preferences.getString(applicationUserName, "");
        }
        else {
            return null;
        }
    }

    /**
     * Returns the current user of the application. User's getter have not been called yet, and
     * need to be called in order for members to be initialized
     * @return The empty User upon success, null if failure
     */
    public User getApplicationUser() {
        if (!isApplicationUserNameSet()) {
            return null;
        }
        return new User(getApplicationUserName(), context);
    }

    /**
     * Works similarly to getApplicationUser, but will return a user with all members initialized
     * ahead of time
     * @return a fully initialized user upon success, null if failure
     */
    public User getFullApplicationUser() {
        User fullUser = getApplicationUser();
        if (fullUser == null) {
            return fullUser;
        }
        try {
            fullUser.getFriends();
            fullUser.getInventory();
            fullUser.getProfile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullUser;

    }

    /**
     * set the application's associated user id
     * @param userID the user id you want to associate with the application
     */
    public void setApplicationUserName(String userID) {
        editor.putString(applicationUserName, userID);
        editor.commit();
    }

    /**
     * Remove the user id from this particular instance of the application
     */
    public void clearApplicaitonUserName() {
        editor.remove(applicationUserName);
        editor.commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    /**
     * {@inheritDoc}
     * @param observer
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * {@inheritDoc}
     * @param observer
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}

