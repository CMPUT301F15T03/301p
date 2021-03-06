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

package ca.ualberta.cmput301.t03.configuration;

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
    private static final String applicationUserNameKey = "APPLICATION_USER_NAME";

    private Context context;
    private Set<Observer> observers;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * Create a new configuration model which shares a common data backend. Called by configuration
     * activity, initialize user activity, and the main activity.
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
                if (key.equals(downloadImagesKey) || key.equals(applicationUserNameKey)) {
                    notifyObservers();
                }
            }
        });
    }

    /**
     * Check if download images is enabled for the application.
     * This is used by the photo model to check determine if photos need to be downloaded.
     *
     * @return Boolean representing if download images setting is enabled or not (true == enabled)
     */
    public Boolean isDownloadImagesEnabled() {
        return preferences.getBoolean(downloadImagesKey, false);
    }

    /**
     * Set the value of download images setting.
     * This is called by the configuration controller when changing settings.
     *
     * @param state true == enabled, false == disable
     */
    public void setDownloadImages(Boolean state) {
        editor.putBoolean(downloadImagesKey, state);
        editor.commit();
    }

    /**
     * Check to see if a user's id has been assigned to this application
     *
     * @return true == user id has been assigned, false == otherwise
     */
    public Boolean isApplicationUserNameSet() {
        return preferences.contains(applicationUserNameKey);
    }

    /**
     * Get the current user id associated with this application
     *
     * @return the application's associated user id
     */
    public String getApplicationUserName() {
        if (isApplicationUserNameSet()) {
            return preferences.getString(applicationUserNameKey, "");
        } else {
            return null;
        }
    }

    /**
     * set the application's associated user id.
     * called by the initialization activity.
     *
     * @param userName the user id you want to associate with the application
     */
    public void setApplicationUserName(String userName) {
        editor.putString(applicationUserNameKey, userName);
        editor.commit();
    }

    /**
     * Remove the user id from this particular instance of the application
     */
    public void clearApplicationUserName() {
        editor.remove(applicationUserNameKey);
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
     *
     * @param observer
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * {@inheritDoc}
     *
     * @param observer
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void clearObservers() {
        observers.clear();
    }
}

