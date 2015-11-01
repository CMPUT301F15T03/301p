package ca.ualberta.cmput301.t03.inventory;

import com.google.gson.annotations.Expose;

import java.util.Collection;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.photo.PhotoGallery;
import ca.ualberta.cmput301.t03.datamanager.DataManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by mmabuyo on 2015-10-29.
 */
public class Item  implements Observer, Observable{
    private PhotoGallery photoList; // we will expose this later
    private Collection<Observer> observers;
    @Expose
    private String itemName;
    @Expose
    private int itemQuantity;
    @Expose
    private String itemQuality;
    @Expose
    private String itemCategory;
    @Expose
    private boolean itemIsPrivate;
    @Expose
    private String itemDescription;

    public Item() {
        observers = new HashSet<>();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemQuality() {
        return itemQuality;
    }

    public void setItemQuality(String itemQuality) {
        this.itemQuality = itemQuality;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public boolean isItemIsPrivate() {
        return itemIsPrivate;
    }

    public void setItemIsPrivate(boolean itemIsPrivate) {
        this.itemIsPrivate = itemIsPrivate;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void commitChanges() {
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

    @Override
    public void update(Observable observable) {
        notifyObservers();
        // todo this likely has something to do with one of the photos changing
    }
}
