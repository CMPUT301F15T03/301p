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

package ca.ualberta.cmput301.t03.trading;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.datamanager.CachedDataManager;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeModificationException;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;
import ca.ualberta.cmput301.t03.user.User;

/**
 * Created by ross on 15-10-29.
 */
public class Trade implements Observable, Observer {
    private TradeState state;
    private User borrower;
    private User owner;
    private ArrayList<Item> borrowersItems;
    private ArrayList<Item> ownersItems;
    private UUID tradeUUID;
    private String comments;

    private DataManager dataManager;
    private Set<Observer> observers;

    public Trade(UUID tradeUUID) {
        this.tradeUUID = tradeUUID;
        this.load();
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
        // this.save();
    }

    public void load() {
        throw new NotImplementedException();
    }

    public void save() {
        throw new NotImplementedException();
    }

    public Boolean isClosed() {
        return state.isClosed();
    }

    public Boolean isOpen() {
        return state.isOpen();
    }

    public TradeState getState() {
        return state;
    }

    public void setState(TradeState state) {
        this.state = state;
    }

    public User getBorrower() {
        return this.borrower;
    }

    public User getOwner() {
        return this.owner;
    }

    public ArrayList<Item> getBorrowersItems() {
        return this.borrowersItems;
    }

    public void setBorrowersItems(ArrayList<Item> newBorrowersItems) throws IllegalTradeModificationException {
        if (!state.isEditable()) {
            String msg = String.format("Trade %s in state %s is uneditable",
                    getTradeUUID().toString(), getState().toString());
            throw new IllegalTradeModificationException(msg);
        }
        this.borrowersItems = newBorrowersItems;
    }

    public ArrayList<Item> getOwnersItems() {
        return this.ownersItems;
    }

    public UUID getTradeUUID() {
        return this.tradeUUID;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void offer() throws IllegalTradeStateTransition {
        this.state.offer(this);
    }

    public void cancel() throws IllegalTradeStateTransition {
        this.state.cancel(this);
    }

    public void accept() throws IllegalTradeStateTransition {
        this.state.accept(this);
    }

    public void decline() throws IllegalTradeStateTransition {
        this.state.decline(this);
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

    @Override
    public void update(Observable observable) {
        throw new UnsupportedOperationException();
    }
}
