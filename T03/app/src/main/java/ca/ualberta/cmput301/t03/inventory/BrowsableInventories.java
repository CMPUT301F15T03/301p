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

import ca.ualberta.cmput301.t03.Filter;
import ca.ualberta.cmput301.t03.Filterable;
import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.user.FriendsList;
import ca.ualberta.cmput301.t03.user.User;


/**
 * Model maintains a List of all of the user's friends and a list of all friends inventory items.
 * Will notify the observing user and any others registered as observers of any change.
 */
public class BrowsableInventories implements Filterable<Item>, Observer, Observable {
    private FriendsList friendList;
    private ArrayList<Item> list;

    private Thread constructorThread;
    @Transient
    private HashSet<Observer> observers;

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
                }
            }
        });

    }

    public Thread getConstructorThread() {
        return constructorThread;
    }

    /**
     * Returns thread that can be used to generate updated list of user's friends items async.
     *
     * @return
     */
    public Thread getBrowsables() {
        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (User friend : friendList.getFriends()) {
                        for (Item item : friend.getInventory().getItems().values()) {
                            list.add(item);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Could not get user and associated friendList");
                }
            }
        });
        return worker;
    }

    /**
     * Return list of all user's freinds Items.
     *
     * @return list of all friends items
     */
    public ArrayList<Item> getList() {
        return this.list;
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
    public Item getFilteredItems() {
        throw new UnsupportedOperationException();
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

}
