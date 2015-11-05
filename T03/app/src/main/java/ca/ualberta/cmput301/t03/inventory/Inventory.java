/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of {ApplicationName}
 *
 * {ApplicationName} is free software: you can redistribute it and/or modify
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

package ca.ualberta.cmput301.t03.inventory;

import android.util.Log;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.UUID;

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
    private LinkedHashMap<UUID, Item> items;

    private String ownerName; // is this needed?
    private String ownerId; // is this needed?


    @Transient
    private HashSet<Observer> observers;

    public Inventory() {
        observers = new HashSet<>();
        items = new LinkedHashMap<>();
    }

    public HashMap<UUID, Item> getItems() {
        return items;
    }


    public void setItems(LinkedHashMap<UUID, Item> items) {
        this.items = items;
        for (Item item : items.values()) {
            item.addObserver(this);
        }
    }


    public void addItem(Item item) {
        items.put(item.getUuid(), item);
        item.addObserver(this);
        notifyObservers();
    }

    public void removeItem(Item item) {
        items.remove(item.getUuid() );
        item.removeObserver(this);
        notifyObservers();
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
