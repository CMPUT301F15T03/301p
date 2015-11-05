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

package ca.ualberta.cmput301.t03.user;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ca.ualberta.cmput301.t03.Filter;
import ca.ualberta.cmput301.t03.Filterable;
import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;

/**
 * Created by ross on 15-10-29.
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

    public ArrayList<User> getFriends() {
        return friends;
    }


    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public boolean containsFriend(User user){
        return friends.contains(user);
    }

    public void addFriend(User user) {
        friends.add(user);
        notifyObservers();
    }

    public void removeFriend(User user) {
        friends.remove(user);
        notifyObservers();
    }

    public void commitChanges() {
        notifyObservers();
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
    public Object getFilteredItems() {
        throw new UnsupportedOperationException();
    }

    public int size(){
        return friends.size();
    }
}
