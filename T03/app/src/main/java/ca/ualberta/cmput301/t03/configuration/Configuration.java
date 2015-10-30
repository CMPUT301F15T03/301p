/*
    Copyright (C) 2015 Kyle O'Shaughnessy
    Photography equipment trading application for CMPUT 301 at the University of Alberta


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

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;

public class Configuration implements Observable {

    private transient static final String key = "CONFIGURATION";
    private transient Context context;
    private transient Set<Observer> observers;
    private Boolean offlineModeEnabled;
    private Boolean downloadImagesEnabled;

    public Configuration(Context context) {
        this.observers = new HashSet<>();
        this.context = context;
        load();
    }

    void load() {
        offlineModeEnabled = false;
        downloadImagesEnabled = false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String stateJson = preferences.getString(key, "");
        if (!stateJson.equals("")) {
            Gson gson = new Gson();
            Configuration temp = gson.fromJson(stateJson, Configuration.class);
            offlineModeEnabled = temp.getOfflineModeEnabled();
            downloadImagesEnabled = temp.getDownloadImagesEnabled();
        }
        notifyObservers();
    }

    void save() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        Gson gson = new Gson();
        preferencesEditor.putString(key, gson.toJson(this));
        preferencesEditor.commit();
    }

    public Boolean getOfflineModeEnabled() {
        return offlineModeEnabled;
    }

    public void setOfflineModeEnabled(Boolean offlineModeEnabled) {
        this.offlineModeEnabled = offlineModeEnabled;
        notifyObservers();
        save();
    }

    public Boolean getDownloadImagesEnabled() {
        return downloadImagesEnabled;
    }

    public void setDownloadImagesEnabled(Boolean downloadImagesEnabled) {
        this.downloadImagesEnabled = downloadImagesEnabled;
        notifyObservers();
        save();
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

