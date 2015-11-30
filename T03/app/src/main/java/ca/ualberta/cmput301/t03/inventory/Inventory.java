/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
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

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import ca.ualberta.cmput301.t03.filters.CollectionFilter;
import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.filters.Filterable;
import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.filters.item_criteria.PrivateFilterCriteria;

/**
 * Inventory maintains a collection of items and will notify the observing user of any changes.
 * Represents the main model for the userinventory workflow and the browse inventories workflow.
 */
@Parcel
public class Inventory implements Filterable<Item>, Observable, Observer, Adaptable, Iterable<Item> {
    public final static String type = "Inventory";
    @Expose
    private ArrayList<Item> items;

    @Transient
    private HashSet<Observer> observers;

    private ArrayList<FilterCriteria> filters;

    /**
     * Should be constructed by the systems singleton or a User itself via the getInventory method,
     * and further managed by the data manager.
     *
     * Creates a new inventory that can be watched and added/removed to/from.
     */
    public Inventory() {
        observers = new HashSet<>();
        items = new ArrayList<>();
        filters = new ArrayList<FilterCriteria>();
    }

    /**
     * Get the hashmap of items.
     * Used by anyone needing to grab the inventory. Examples: BrowsingFriends, Browsing Self.
     * @return the hashmap of items
     */
    public HashMap<UUID, Item> getItems() {
        LinkedHashMap<UUID, Item> filteredItems = new LinkedHashMap<>();
//        for (Item item: this.items){
//            filteredItems.put(item.getUuid(), item);
//        }
        ArrayList<Item> filteredList = getFilteredItems(this.items, this.filters);
        for (Item item: filteredList){
            filteredItems.put(item.getUuid(), item);
        }

        return filteredItems;
    }

    public Item get(int index){
        return items.get(index);
    }

    /**
     * Set the items, used by GSON and the data manager when loading the inventory.
     *
     * @param items items to be set
     */
    public void setItems(LinkedHashMap<UUID, Item> items) {
        this.items = new ArrayList<>(items.values());
        for (Item item : items.values()) {
            item.addObserver(this);
        }
        notifyObservers();
    }

    /**
     * Get an Item from the inventory model.
     * Examples usages: Inspecting an Item, drawing items to ListView in inventory obtaining item data in a trade.
     *
     * @param itemUUID the item's UUID
     * @return the item
     */
    public Item getItem(UUID itemUUID) {
        return getItems().get(itemUUID);
    }

    /**
     * Get an Item from the inventory model.
     * Examples usages: Inspecting an Item, drawing items to ListView in inventory obtaining item data in a trade.
     *
     * @param itemUUID the item's string representation of the UUID
     * @return the item
     */
    public Item getItem(String itemUUID) {
        return getItems().get(UUID.fromString(itemUUID));
    }

    /**
     * Add an item to inventory.
     * Used during creation of items.
     * @param item item to add
     */
    public void addItem(Item item) {
        items.add(item);
        item.addObserver(this);
        notifyObservers();
    }

    /**
     * remove an item from inventory
     * User may delete items from inventory.
     * @param item item to remove
     */
    public void removeItem(Item item) {
        items.remove(item);
        item.clearPhotoList();
        item.removeObserver(this);
        notifyObservers();
    }

    /**
     * alias for notifyObservers, call this after modifying the inventory.
     */
    public void commitChanges() {
        notifyObservers();
    }

    /**
     * {@inheritDoc}
     *
     * @param filter the filter you wish to apply
     */
    @Override
    public void addFilter(FilterCriteria filter) {
        filters.add(filter);

    }

    /**
     * {@inheritDoc}
     *
     * @param filterName the filter you wish to remove
     */
    @Override
    public void removeFilter(String filterName) {
        for (FilterCriteria filter: filters){
            if (filter.getName().equals(filterName)){
                filters.remove(filter);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param filters the filter you wish to remove
     */
    @Override
    public void setFilters(ArrayList<FilterCriteria> filters){
        this.filters = filters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearFilters() {
        this.filters = new ArrayList<FilterCriteria>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Item> getFilteredItems(ArrayList<Item> list, List<FilterCriteria> filters) {
        CollectionFilter<ArrayList> collectionFilter = new CollectionFilter<ArrayList>();
        for(FilterCriteria filter: filters){
            collectionFilter.addFilterCriteria(filter);
        }
        return collectionFilter.filterCopy(list);
    }

    @Override
    public List<FilterCriteria> getFilters() {
        return filters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param observer the Observer to add
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * {@inheritDoc}
     *
     * @param observer the Observer to remove
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void clearObservers() {
        observers.clear();
    }

    /**
     * {@inheritDoc}
     *
     * @param observable reference to the Observable that triggered the update()
     */
    @Override
    public void update(Observable observable) {

        notifyObservers();
    }

    public HashSet<Observer> getObservers() {
        return observers;
    }

    @Override
    public List<Item> getAdaptableItems() {
        ArrayList<Item> i = new ArrayList<>();
        for (Item item: getItems().values()){
            i.add(item);
        }
        Collections.sort(i);
        return i;
    }

    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }

    public int size(){
        return items.size();
    }
}
