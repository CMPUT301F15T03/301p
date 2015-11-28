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
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.CachedDataManager;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.BrowsableInventories;
import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.trading.Trade;
import ca.ualberta.cmput301.t03.trading.TradeList;

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
    private TradeList tradeList;
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
     * ex.
     *  User u = new User("john12345");
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
     * Sets up a datamanager that will handle backing up data to the local cache and Elastic Search.
     * Is called by the PrimaryUser singleton.
     *
     * ex.
     *  User u = new User("john12345", getActivity().getApplicationContext());
     *
     * @param username A string that uniquely identifies a User.
     * @param context  The current application context.
     */
    public User(String username, Context context) {
        this.observers = new HashSet<>();
        this.context = context;
        this.dataManager = TradeApp.getInstance().createDataManager(true);
        this.username = username;
    }

    /**
     * Alternate constructor for a User.
     *
     * Should only be used on a freshly Unparceled user, in order to restore context.
     *
     * Use the default constructor in all other cases.
     *
     * ex.
     *  User u = Parcels.unwrap(getArguments().getParcelable(ARG_PARAM1));
     *  User u2 = new User(u, getContext());
     *
     *
     * @param user An unparceled User; will not be fully initialized.
     * @param context The current application contest.
     */
    public User(User user, Context context) {
        this(user.getUsername(), context);
    }


    /**
     * Gets the User's username. This will not hit the network.
     *
     * This should be called by the view to display the current username.
     *
     * @return the User's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the User's FriendsList.
     * <p>
     *
     * This should be called by the view to display the friends list,
     * and by the controller to get a reference to the friends list model.
     *
     * WARNING: This might hit the network! It must be
     * run asynchronously.
     *
     * @return FriendsList containing the User's Friends.
     * @throws IOException
     */
    public FriendsList getFriends() throws IOException, ServiceNotAvailableException {
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
     * <p>
     *
     * This should be called by the view to display the profile,
     * and by the controller to get a reference to the UserProfile model.
     *
     * WARNING: This might hit the network! It must be run
     * asynchronously.
     *
     * @return UserProfile containing the User's profile information.
     * @throws IOException
     */
    public UserProfile getProfile() throws IOException, ServiceNotAvailableException {
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
     * <p>
     * This should be called by the view to display inventory,
     * and by the controller to get a reference to the inventory model.
     *
     * WARNING: This might hit the network! It must be run
     * asynchronously.
     *
     * @return Inventory populated with the User's items.
     * @throws IOException
     */
    public Inventory getInventory() throws IOException, ServiceNotAvailableException {
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


    public BrowsableInventories getBrowseableInventories() throws IOException, ServiceNotAvailableException {

        BrowsableInventories newInventory = new BrowsableInventories();
        FriendsList list = getFriends();
        for (User f: list){
            for (Item i: f.getInventory()){
                newInventory.addItem(i);
                try{
                    i.getPhotoList().getPhotos().get(0).getPhoto(); //cache first photo!
                } catch(IndexOutOfBoundsException e){
                    // no problemo.
                }
            }
        }
        return newInventory;
    }

    /**
     * Get TradeList of trades which the User is involved in.
     * <p>
     * This should be called by the view to display trades,
     * and by the controller to get a reference to the trades model.
     *
     * WARNING: This might hit the network! It must be run
     * asynchronously.
     *
     * @return TradeList: trades which the user is involved in
     * @throws IOException
     */
    public TradeList getTradeList() throws IOException, ServiceNotAvailableException {
        DataKey key = new DataKey(TradeList.type, username);
        if (!dataManager.keyExists(key)) {
            tradeList = new TradeList();
            dataManager.writeData(key, tradeList, TradeList.class);
        } else {
            tradeList = dataManager.getData(key, TradeList.class);
        }
        ArrayList<Trade> temp = new ArrayList<>();
        for (Trade t : tradeList.getTradesAsList()) {
            temp.add(new Trade(t.getTradeUUID(), context));
        }
        tradeList.clear();
        tradeList.addAll(temp);

        tradeList.addObserver(this);
        return tradeList;
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

    @Override
    public void clearObservers() {
        observers.clear();
    }

    /**
     * When called by the an Observable, a dump of the field's data will happen
     * using a DataManager.
     */
    @Override
    public void update(Observable observable) {
        final Observable o = observable;
        try {
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
            } else if (o == tradeList) {
                try {
                    /**
                     * TODO solve tradeList update race condition:
                     * - fetch tradeList again
                     * - union the fetched tradeList and the local tradeList
                     * - store the union as the local tradeList
                     * - write the local tradeList
                     */
                    dataManager.writeData(new DataKey(TradeList.type, username), tradeList, TradeList.class);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to write trade list changes.");
                }
            } else {
//                throw new RuntimeException("No rule found to update User using Observable: " + o.getClass());
            }
        }
        catch (ServiceNotAvailableException e) {
            throw new RuntimeException("App is offline.", e);
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

    public void refresh() throws IOException, ServiceNotAvailableException {
        if (friends != null) {
            for (User friend : friends.getFriends()) {
                friend.refresh();
            }
        }
        if (profile != null) {
            HashSet<Observer> backup = profile.getObservers();
            profile = null;
            getProfile();
            for (Observer o : backup) {
                profile.addObserver(o);
            }
        }
        if (inventory != null) {
            HashSet<Observer> backup = inventory.getObservers();
            inventory = null;
            getInventory();
            for (Observer o : backup) {
                inventory.addObserver(o);
            }
        }
        if (tradeList != null) {
            HashSet<Observer> backup = tradeList.getObservers();
            tradeList = null;
            getTradeList();
            for (Observer o : backup) {
                tradeList.addObserver(o);
            }
        }
    }
}
