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

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;

/**
 * Model component of the Configuration MVC triplet.
 * This model will handle maintenance of the application wide settings which others can call upon.
 */
public class Configuration implements Observable {

    private static final String downloadImagesKey = "DOWNLOAD_IMAGES_ENABLED";
    private static final String applicationUserKey = "APPLICATION_USER_ID";

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
        preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(downloadImagesKey) || key.equals(applicationUserKey)) {
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
    public Boolean isApplicationUserIDCreated() {
        return preferences.contains(applicationUserKey);
    }

    /**
     * Get the current user id associated with this application
     * @return the application's associated user id
     */
    public String getApplicationUserID() {
        if (isApplicationUserIDCreated()) {
            return preferences.getString(applicationUserKey, "");
        }
        else {
            return null;
        }
    }

    /**
     * set the application's associated user id
     * @param userID the user id you want to associate with the application
     */
    public void setApplicationUserID(String userID) {
        editor.putString(applicationUserKey, userID);
        editor.commit();
    }

    /**
     * Remove the user id from this particular instance of the application
     */
    public void clearApplicaitonUserID() {
        editor.remove(applicationUserKey);
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

