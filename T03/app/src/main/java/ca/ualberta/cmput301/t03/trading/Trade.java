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

package ca.ualberta.cmput301.t03.trading;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.datamanager.CachedDataManager;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeModificationException;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;
import ca.ualberta.cmput301.t03.user.User;

/**
 * Model that represents a single trade.
 */
public class Trade implements Observable {
    public final static String type = "Trade";
    @Expose
    private TradeState state;
    @Expose
    private User borrower;
    @Expose
    private User owner;
    @Expose
    private ArrayList<Item> borrowersItems;
    @Expose
    private ArrayList<Item> ownersItems;
    @Expose
    private UUID tradeUUID;
    @Expose
    private String comments;

    private DataManager dataManager;
    private Set<Observer> observers;

    public Trade(UUID tradeUUID, Context context) {
        this.tradeUUID = tradeUUID;
        this.load();

        this.dataManager = new CachedDataManager(new HttpDataManager(context, true), context, true);
        this.observers = new HashSet<>();
    }

    public Trade(User borrower, User owner,
                 Collection<Item> borrowersItems, Collection<Item> ownersItems,
                 Context context) {
        this.borrower = borrower;
        this.owner = owner;
        this.borrowersItems = new ArrayList<>();
        this.ownersItems = new ArrayList<>();
        for (Item item : borrowersItems) {
            this.borrowersItems.add(item);
        }
        for (Item item : ownersItems) {
            this.ownersItems.add(item);
        }

        this.dataManager = new CachedDataManager(new HttpDataManager(context, true), context, true);
        this.observers = new HashSet<>();

        this.tradeUUID = UUID.randomUUID();
        this.commitChanges();
    }

    private void load() {
        DataKey key = new DataKey(Trade.type, this.getTradeUUID().toString());
        try {
            if (dataManager.keyExists(key)) {
                Trade t = dataManager.getData(key, Trade.class);
                this.setState(t.getState());
                this.setBorrowersItems(t.getBorrowersItems());
                this.setComments(t.getComments());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load trade with UUID " + this.getTradeUUID().toString());
        } catch (IllegalTradeModificationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void save() {
        DataKey key = new DataKey(Trade.type, this.getTradeUUID().toString());
        try {
            dataManager.writeData(key, this, Trade.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save trade with UUID " + this.getTradeUUID().toString());
        }
    }

    public void commitChanges() {
        save();
        notifyObservers();
    }

    public Boolean isClosed() {
        return getState().isClosed();
    }

    public Boolean isOpen() {
        return getState().isOpen();
    }

    public Boolean isEditable() {
        return getState().isEditable();
    }

    public TradeState getState() {
        this.load();
        return state;
    }

    public void setState(TradeState state) {
        this.state = state;
        this.commitChanges();
    }

    public User getBorrower() {
        return this.borrower;
    }

    public User getOwner() {
        return this.owner;
    }

    public ArrayList<Item> getBorrowersItems() {
        this.load();
        return this.borrowersItems;
    }

    public void setBorrowersItems(ArrayList<Item> newBorrowersItems) throws IllegalTradeModificationException {
        if (!state.isEditable()) {
            String msg = String.format("Trade %s in state %s is uneditable",
                    getTradeUUID().toString(), getState().toString());
            throw new IllegalTradeModificationException(msg);
        }
        this.borrowersItems = newBorrowersItems;
        this.commitChanges();
    }

    public ArrayList<Item> getOwnersItems() {
        return this.ownersItems;
    }

    public UUID getTradeUUID() {
        return this.tradeUUID;
    }

    public String getComments() {
        this.load();
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
        this.commitChanges();
    }

    public void offer() throws IllegalTradeStateTransition {
        getState().offer(this);
    }

    public void cancel() throws IllegalTradeStateTransition {
        getState().cancel(this);
    }

    public void accept() throws IllegalTradeStateTransition {
        getState().accept(this);
    }

    public void decline() throws IllegalTradeStateTransition {
        getState().decline(this);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
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
}
