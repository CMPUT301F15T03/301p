package ca.ualberta.cmput301.t03.trading;

import java.util.*;

import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.user.User;

/**
 * Created by ross on 15-10-29.
 */
public class Trade implements ca.ualberta.cmput301.t03.Observable, ca.ualberta.cmput301.t03.Observer {
    private TradeState state;
    private User borrower;
    private User owner;
    private ArrayList<Item> borrowersItems;
    private ArrayList<Item> ownersItems;
//    private Guid id;
    private String comments;

    private DataManager dataManager;
    private Set<ca.ualberta.cmput301.t03.Observer> observers;

    public void load() {
        throw new UnsupportedOperationException();
    }
    public void save() {
        throw new UnsupportedOperationException();
    }

    public Boolean isClosed() {
        throw new UnsupportedOperationException();
    }
    public Boolean isOpen() {
        throw new UnsupportedOperationException();
    }
    public TradeState getState() {
        throw new UnsupportedOperationException();
    }
    public void setState(TradeState state) {
        throw new UnsupportedOperationException();
    }

    public User getBorrower() {
        throw new UnsupportedOperationException();
    }
    public User getOwner() {
        throw new UnsupportedOperationException();
    }
    public ArrayList<Item> getBorrowersItems() {
        throw new UnsupportedOperationException();
    }
    public ArrayList<Item> getOwnersItems() {
        throw new UnsupportedOperationException();
    }
//    public Guid getId() {
//        return this.id;
//    }
    public String getComments() {
        return this.comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public void offer() {
        this.state.offer(this);
    }
    public void cancel() {
        this.state.cancel(this);
    }
    public void accept() {
        this.state.accept(this);
    }
    public void decline() {
        this.state.decline(this);
    }
    private void invalidate() {
        throw new UnsupportedOperationException();
    }


    @Override
    public void notifyObservers() {
        for (ca.ualberta.cmput301.t03.Observer o: observers) {
            o.update(this);
        }
    }

    @Override
    public void addObserver(ca.ualberta.cmput301.t03.Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ca.ualberta.cmput301.t03.Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void update(ca.ualberta.cmput301.t03.Observable observable) {
        throw new UnsupportedOperationException();
    }
}