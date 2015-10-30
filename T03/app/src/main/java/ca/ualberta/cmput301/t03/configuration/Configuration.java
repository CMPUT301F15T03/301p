package ca.ualberta.cmput301.t03.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.datamanager.DataManager;

/**
 * Created by kyleoshaughnessy on 2015-10-29.
 */
public class Configuration implements Observable{

    private transient Context context;
    private transient Set<Observer> observers;
    private Boolean offlineModeEnabled;
    private Boolean downloadImagesEnabled;

    private transient static final String key = "CONFIGURATION";

    public Configuration(Context context) {
        this.observers = new HashSet<>();
        this.context = context;
        load();
    }

    void load() {
        offlineModeEnabled = false;
        downloadImagesEnabled = false;
        // TODO : get values for offlineModeEnabled and downloadImagesEnabled
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
        // TODO : save values of offlineModeEnabled and downloadImagesEnabled
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

