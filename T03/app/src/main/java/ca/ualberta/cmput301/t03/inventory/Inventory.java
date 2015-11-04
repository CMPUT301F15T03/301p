package ca.ualberta.cmput301.t03.inventory;

import android.util.Log;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import ca.ualberta.cmput301.t03.Filter;
import ca.ualberta.cmput301.t03.Filterable;
import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.datamanager.DataManager;

/**
 * Created by ross on 15-10-29.
 */
@Parcel
public class Inventory implements Filterable<Item>, Observable, Observer {
    public final static String type = "Inventory";
    @Expose
    private ArrayList<Item> items;

    private String ownerName; // is this needed?
    private String ownerId; // is this needed?
    @Transient
    private HashSet<Observer> observers;

    public Inventory() {
        observers = new HashSet<>();
        items = new ArrayList<>();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
        for (Item item : items) {
            item.addObserver(this);
        }
    }

    public void addItem(Item item) {
        items.add(item);
        item.addObserver(this);
        notifyObservers();
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void commitChanges() {
        notifyObservers();
    }

    @Override
    public void addFilter(Filter filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeFilter(Filter filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearFilters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item getFilteredItems() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void notifyObservers() {
        for (Observer o: observers) {
            Log.d("Q", "inventory is notifying " + o.toString());
            o.update(this);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        Log.d("Q", "adding " + observer.toString() + " to Inventory");
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void update(Observable observable) {

        notifyObservers();
    }

}
