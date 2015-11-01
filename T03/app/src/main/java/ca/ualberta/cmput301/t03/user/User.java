package ca.ualberta.cmput301.t03.user;

import java.util.HashSet;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.inventory.BrowsableInventories;
import ca.ualberta.cmput301.t03.inventory.Inventory;

/**
 * Created by ross on 15-10-29.
 */
public class User implements Observable, Observer {

    private String username;
    private FriendsList friends;
    private UserProfile profile;
    private Inventory inventory;
    private BrowsableInventories browsableInventories; // not sure we need this

    private DataManager dataManager;
    private HashSet<Observer> observers;

    public User(String username) {
        this.username = username;
    }

    public FriendsList getFriends() {
        if (friends == null) {
            friends = new FriendsList();
            // todo : make a persistent copy to a dataManager
            friends.addObserver(this);
        }
        return friends;
    }

    public UserProfile getProfile() {
        if (profile == null) {
            profile = new UserProfile();
            // todo : make a persistent copy to a dataManager
            profile.addObserver(this);
        }
        return profile;
    }

    public Inventory getInventory() {
        if (inventory == null) {
            inventory = new Inventory();
            // todo : make a persistent copy to a dataManager
            inventory.addObserver(this);
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
        // todo : once we get an update from one of our members, push the update to a dataManager
//        throw new UnsupportedOperationException();
    }
}
