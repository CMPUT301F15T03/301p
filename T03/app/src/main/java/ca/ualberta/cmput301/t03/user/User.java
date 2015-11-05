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

import android.content.Context;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Transient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.datamanager.CachedDataManager;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.BrowsableInventories;
import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.trading.Trade;

/**
 * Model that represents application Users.
 * The current user is a User, and all of his/her Friends are Users too.
 */
@Parcel
public class User implements Observable, Observer, Comparable<User> {

    @Expose
    private String username;
    @Transient
    private FriendsList friends;
    @Transient
    private UserProfile profile;
    @Transient
    private Inventory inventory;
    @Transient
    private LinkedHashMap<UUID, Trade> trades;
    @Transient
    private BrowsableInventories browsableInventories; // not sure we need this
    @Transient
    private DataManager dataManager;
    @Transient
    private Context context;
    @Transient
    private HashSet<Observer> observers;

    /**
     * Constructor that should only be used
     * for Parceling and Unparceling {@link Parcel}
     *
     * @param username The User's username.
     */
    @ParcelConstructor
    private User(String username) {
        /**
         * Context is un-Parcelable, and on unmarshal/deserialize we only want
         * read-only access anyway, so we just get the username.
         */
        this.observers = null;
        this.context = null;
        this.dataManager = null;
        this.username = username;
    }

    /**
     * Default constructor for a User.
     *
     * @param username A string that uniquely identifies a User.
     * @param context  The current application context.
     */
    public User(String username, Context context) {
        this.observers = new HashSet<>();
        this.context = context;
        this.dataManager = new CachedDataManager(new HttpDataManager(context, true), context, true);
        this.username = username;
    }

    /**
     * Gets the User's username. This will not hit the network.
     *
     * @return the User's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the User's FriendsList.
     * <p/>
     * WARNING: This might hit the network! It must be
     * run asynchronously.
     *
     * @return FriendsList containing the User's Friends.
     * @throws IOException
     */
    public FriendsList getFriends() throws IOException {
        if (friends == null) {
            DataKey key = new DataKey(FriendsList.type, username);
            if (!dataManager.keyExists(key)) {
                friends = new FriendsList();
                dataManager.writeData(key, friends, FriendsList.class);
            } else {
                friends = dataManager.getData(key, FriendsList.class);
            }
            ArrayList<User> temp = new ArrayList<>();
            for (User user : friends.getFriends()) {
                temp.add(new User(user.getUsername(), context));
            }
            friends.getFriends().clear();
            friends.getFriends().addAll(temp);
            friends.addObserver(this);
        }
        return friends;
    }

    /**
     * Get the User's UserProfile.
     * <p/>
     * WARNING: This might hit the network! It must be run
     * asynchronously.
     *
     * @return UserProfile containing the User's profile information.
     * @throws IOException
     */
    public UserProfile getProfile() throws IOException {
        if (profile == null) {
            DataKey key = new DataKey(UserProfile.type, username);
            if (!dataManager.keyExists(key)) {
                profile = new UserProfile();
                dataManager.writeData(key, profile, UserProfile.class);
            } else {
                profile = dataManager.getData(key, UserProfile.class);
            }
            profile.addObserver(this);
        }
        return profile;
    }

    /**
     * Get the User's Inventory.
     * <p/>
     * WARNING: This might hit the network! It must be run
     * asynchronously.
     *
     * @return Inventory populated with the User's items.
     * @throws IOException
     */
    public Inventory getInventory() throws IOException {
        if (inventory == null) {
            DataKey key = new DataKey(Inventory.type, username);
            if (!dataManager.keyExists(key)) {
                inventory = new Inventory();
                dataManager.writeData(key, inventory, Inventory.class);
            } else {
                inventory = dataManager.getData(key, Inventory.class);
            }
            inventory.addObserver(this);
        }
        for (Item item : inventory.getItems().values()) {
            item.addObserver(inventory);
        }
        return inventory;
    }

    /**
     * Get Trades which the User is involved in.
     * <p/>
     * WARNING: This might hit the network! It must be run
     * asynchronously.
     *
     * @return LinkedHashMap of Trades the user is involved in.
     *         This is a map of tradeUUID->Trade.
     * @throws IOException
     */
    public LinkedHashMap<UUID, Trade> getTrades() throws IOException {
        if (trades == null) {
            DataKey key = new DataKey(Trade.type, username);
            if (!dataManager.keyExists(key)) {
                trades = new LinkedHashMap<>();
                dataManager.writeData(key, trades, Trade.class);
            } else {
                trades = dataManager.getData(key, Trade.class);
            }
        }
        return trades;
    }

    /**
     * Set Trades which the User is involved in.
     *
     * @param trades Trades the User is involved in
     */
    public void setTrades(LinkedHashMap<UUID, Trade> trades) {
        this.trades = trades;
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
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * When called by the fields that User is observing, a dump of the field's data will happen
     * using a DataManager.
     */
    @Override
    public void update(Observable observable) {
        final Observable o = observable;
        if (o == friends) {
            try {
                dataManager.writeData(new DataKey(FriendsList.type, username), friends, FriendsList.class);
            } catch (IOException e) {
                throw new RuntimeException("Unable to write friendsList changes.");
            }
        } else if (o == inventory) {
            try {
                dataManager.writeData(new DataKey(Inventory.type, username), inventory, Inventory.class);
            } catch (IOException e) {
                throw new RuntimeException("Unable to write inventory changes.");
            }
        } else if (o == profile) {
            try {
                dataManager.writeData(new DataKey(UserProfile.type, username), profile, UserProfile.class);
            } catch (IOException e) {
                throw new RuntimeException("Unable to write profile changes.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%s", getUsername());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(User another) {
        return getUsername().compareTo(another.getUsername());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        }

        User other = (User) o;

        return compareTo(other) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
