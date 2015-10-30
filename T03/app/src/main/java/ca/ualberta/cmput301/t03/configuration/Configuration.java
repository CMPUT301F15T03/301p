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

import java.util.HashSet;
import java.util.Set;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;

public class Configuration implements Observable {

    private static final String offlineModeKey = "OFFLINE_MODE_ENABLED";
    private static final String downloadImagesKey = "DOWNLOAD_IMAGES_ENABLED";

    private Set<Observer> observers;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


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

    public Boolean isOfflineModeEnabled() {
        return preferences.getBoolean(offlineModeKey, false);
    }

    public void setOfflineMode(Boolean offlineMode) {
        editor.putBoolean(offlineModeKey, offlineMode);
        editor.commit();
    }

    public Boolean isDownloadImagesEnabled() {
        return preferences.getBoolean(downloadImagesKey, false);
    }

    public void setDownloadImages(Boolean downloadImages) {
        editor.putBoolean(downloadImagesKey, downloadImages);
        editor.commit();
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}

