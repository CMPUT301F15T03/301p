package ca.ualberta.cmput301.t03.inventory;

import com.google.gson.annotations.Expose;

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
public class Inventory implements Filterable<Item>, Observable, Observer {
    public final static String type = "Inventory";
    @Expose
    private ArrayList<Item> items;

    private String ownerName; // is this needed?
    private String ownerId; // is this needed?
    private HashSet<Observer> observers;

    public Inventory() {
        observers = new HashSet<>();
        items = new ArrayList<>();
        items.add(new Item("testItem", "testItem"));
        items.add(new Item("testItem2", "testItem2"));
        items.add(new Item("testItem3", "testItem3"));
        items.add(new Item("testItem4", "testItem4"));
        items.add(new Item("testItem5", "testItem5"));
        items.add(new Item("testItem6", "testItem6"));
        items.add(new Item("testItem7", "testItem7"));
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
            o.update(this);
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
    }
}
