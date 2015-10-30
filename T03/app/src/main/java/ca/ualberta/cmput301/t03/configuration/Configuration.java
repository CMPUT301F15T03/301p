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

import java.lang.annotation.Documented;
import java.util.HashSet;
import java.util.Set;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;

/**
 * Model component of the Configuration MVC triplet.
 * This model will handle maintenance of the application wide settings which others can call upon.
 */
public class Configuration implements Observable {

    private static final String offlineModeKey = "OFFLINE_MODE_ENABLED";
    private static final String downloadImagesKey = "DOWNLOAD_IMAGES_ENABLED";

    private Set<Observer> observers;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     *
     * @param context Application context provided by the caller to allow for persistent storage.
     */
    public Configuration(Context context) {
        this.observers = new HashSet<>();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = preferences.edit();
        preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(offlineModeKey) || key.equals(downloadImagesKey)) {
                    notifyObservers();
                }
            }
        });
    }

    /**
     * Check if offline mode is enabled for the application
     * @return Boolean representing if offline mode is enabled or not (true == enabled)
     */
    public Boolean isOfflineModeEnabled() {
        return preferences.getBoolean(offlineModeKey, false);
    }

    /**
     * Set the value of offline mode
     * @param offlineMode true == enabled, false == disable
     */
    public void setOfflineMode(Boolean offlineMode) {
        editor.putBoolean(offlineModeKey, offlineMode);
        editor.commit();
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
     * @param downloadImages true == enabled, false == disable
     */
    public void setDownloadImages(Boolean downloadImages) {
        editor.putBoolean(downloadImagesKey, downloadImages);
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

