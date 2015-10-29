package ca.ualberta.cmput301.t03;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kyleoshaughnessy on 2015-10-29.
 */
public class Configuration implements Observable{
    private DataManager dataManager;
    private Set<Observer> observers;
    private Boolean offlineModeEnabled;
    private Boolean downloadImagesEnabled; 

    public Configuration() {
        dataManager = new DataManager();
        observers = new HashSet<>();
        load();
    }

    void load() {
        // TODO : get values for offlineModeEnabled and downloadImagesEnabled
        notifyObservers();
    }

    void save() {
        // TODO : save to the dataManager
    }

    public Boolean getOfflineModeEnabled() {
        return offlineModeEnabled;
    }

    public void setOfflineModeEnabled(Boolean offlineModeEnabled) {
        this.offlineModeEnabled = offlineModeEnabled;
        notifyObservers();
    }

    public Boolean getDownloadImagesEnabled() {
        return downloadImagesEnabled;
    }

    public void setDownloadImagesEnabled(Boolean downloadImagesEnabled) {
        this.downloadImagesEnabled = downloadImagesEnabled;
        notifyObservers();
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

