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

package ca.ualberta.cmput301.t03.user;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashSet;

import ca.ualberta.cmput301.t03.Filter;
import ca.ualberta.cmput301.t03.Filterable;
import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;

/**
 * Friendslist maintains a list of Users that that a User has, Remote data access is handled by the
 * User which owns the friendslist. When a user receives an update from its friendslist, the data
 * is written to the local index and the remote index.
 */
public class FriendsList implements Observable, Filterable {

    public final static String type = "FriendsList";

    @Expose
    private ArrayList<User> friends; // should this be a list or a set?
    private HashSet<Observer> observers;

    public FriendsList() {
        friends = new ArrayList<>();
        observers = new HashSet<>();
    }

    /**
     * Gets the collection of Users representing a friendslist.
     *
     * @return the friends.
     */
    public ArrayList<User> getFriends() {
        return friends;
    }

    /**
     * Set the friends, called by GSON mainly
     *
     * @param friends friends to set
     */
    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    /**
     * See if the friendslist has the user in it
     *
     * @param user the user we are checking
     * @return true == contains, false == does not contain
     */
    public boolean containsFriend(User user) {
        return friends.contains(user);
    }

    /**
     * Add a friend to the friendslist
     *
     * @param user user to add
     */
    public void addFriend(User user) {
        friends.add(user);
        notifyObservers();
    }

    /**
     * remove a friend
     *
     * @param user use rot remove
     */
    public void removeFriend(User user) {
        friends.remove(user);
        notifyObservers();
    }

    /**
     * alias for notifyObservers, to be called after modifying the friendslist
     */
    public void commitChanges() {
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

    /**
     * {@inheritDoc}
     *
     * @param filter the filter you wish to apply
     */
    @Override
    public void addFilter(Filter filter) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @param filter the filter you wish to remove
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
    public Object getFilteredItems() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the number of friends.
     */
    public int size() {
        return friends.size();
    }
}
