package ca.ualberta.cmput301.t03.user;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.CalendarContract;

import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.CachedDataManager;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.LocalDataManager;
import ca.ualberta.cmput301.t03.datamanager.httpdatamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.BrowsableInventories;
import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.inventory.Item;

/**
 * Created by ross on 15-10-29.
 */
public class User implements Observable, Observer, Comparable<User> {

    public String getUsername() {
        return username;
    }

    @Expose
    private String username;

    private FriendsList friends;
    private UserProfile profile;
    private Inventory inventory;
    private BrowsableInventories browsableInventories; // not sure we need this

    private DataManager dataManager;
    private Context context;
    private HashSet<Observer> observers;

    public User(String username, Context context) throws MalformedURLException {
        this.observers = new HashSet<>();
        this.context = context;
        this.dataManager = new CachedDataManager(new HttpDataManager(context, true), context, true);
        this.username = username;
    }

    public FriendsList getFriends() throws IOException {
        if (friends == null) {
            DataKey key = new DataKey(FriendsList.type, username);
            if (!dataManager.keyExists(key)) {
                friends = new FriendsList();
                dataManager.writeData(key, friends, FriendsList.class);
            }
            else {
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

    public UserProfile getProfile() throws IOException {
        if (profile == null) {
            DataKey key = new DataKey(UserProfile.type, username);
            if (!dataManager.keyExists(key)) {
                profile = new UserProfile();
                dataManager.writeData(key, profile, UserProfile.class);
            }
            else {
                profile = dataManager.getData(key, UserProfile.class);
            }
            profile.addObserver(this);
        }
        return profile;
    }

    public Inventory getInventory() throws IOException {
        if (inventory == null) {
            DataKey key = new DataKey(Inventory.type, username);
            if (!dataManager.keyExists(key)) {
                inventory = new Inventory();
                dataManager.writeData(key, inventory, Inventory.class);
            }
            else {
                inventory = dataManager.getData(key, Inventory.class);
            }
            inventory.addObserver(this);
        }
        for (Item item : inventory.getItems()) {
            item.addObserver(inventory);
        }
        return inventory;
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

    @Override
    public String toString() {
        return String.format("%s", getUsername());
    }

    @Override
    public int compareTo(User another) {
        return getUsername().compareTo(another.getUsername());
    }
}
