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

import org.parceler.Transient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.filters.CollectionFilter;
import ca.ualberta.cmput301.t03.filters.Filter;
import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.filters.Filterable;
import ca.ualberta.cmput301.t03.filters.item_criteria.*;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.user.FriendsList;
import ca.ualberta.cmput301.t03.user.User;


/**
 * Model maintains a List of all of the user's friends and a list of all friends inventory items.
 * Will notify the observing user and any others registered as observers of any change.
 *
 * Used by browsable Views. Also available to anything requiring a list of all tradable items.
 */
public class BrowsableInventories implements Filterable<Item>, Observer, Observable {
    private FriendsList friendList;
    private ArrayList<Item> list;

    private Thread constructorThread;
    @Transient
    private HashSet<Observer> observers;

    /**
     * Constructs the model by fetching current users friends. Must be follow by getBrowsables()
     * to further fetch the friends inventory items.
     */
    public BrowsableInventories() {
        observers = new HashSet<>();
        list = new ArrayList<Item>();
        constructorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    User user = PrimaryUser.getInstance();
                    friendList = user.getFriends();
                } catch (IOException e) {
                    throw new RuntimeException("Could not get user and associated friendList");
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
            }
        });

    }

    public Thread getConstructorThread() {
        return constructorThread;
    }

    /**
     * Returns thread that can be used to generate updated list of user's friends items async.
     * Used to update the model in efforts of fetching all friends items.
     * @return
     */
    public Thread getBrowsables() {
        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (User friend : friendList.getFriends()) {
                        for (Item item : friend.getInventory().getItems().values()) {
                            if (item.getPhotoList().getPhotos().size() > 0) {
                                item.getPhotoList().getPhotos().get(0).downloadPhoto();
                            }
                            list.add(item);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Could not get user and associated friendList");
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
            }
        });
        return worker;
    }

    /**
     * Return list of all user's friends Items.
     *
     * @return list of all friends items
     */
    public ArrayList<Item> getList() {
        List<FilterCriteria> filters = new ArrayList<FilterCriteria>();
        filters.add(new PrivateFilterCriteria());
        return getFilteredItems(this.list, filters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFilter(Filter filter) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFilter(Filter filter) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearFilters() {
        throw new UnsupportedOperationException();
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

    /**
     * {@inheritDoc}
     *
     * @param observable reference to the Observable that triggered the update()
     */
    @Override
    public void update(Observable observable) {
        notifyObservers();
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

}
