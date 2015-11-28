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

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeModificationException;

/**
 * TradeList represents a collection of trades a user is currently involved in. This is the model
 * for the TradeHistory workflow.
 */
public class TradeList implements Observable, Observer {
    public static final String type = "TradeList";

    @Expose
    private HashMap<UUID, Trade> trades;
    private Set<Observer> observers;

    public TradeList() {
        this.trades = new LinkedHashMap<>();
        this.observers = new HashSet<>();
    }

    /**
     * Add a trade to the user.
     *
     * @param trade trade to be added
     */
    public void addTrade(Trade trade) {
        this.trades.put(trade.getTradeUUID(), trade);
        commitChanges();
    }

    /**
     * Add each trade in a list to the user
     *
     * @param trades list of trades to be added
     */
    public void addAll(List<Trade> trades) {
        for (Trade t : trades) {
            this.addTrade(t);
        }
    }

    /**
     * Clear the list of trades. Use carefully.
     */
    public void clear() {
        this.trades = new LinkedHashMap<>();
        this.commitChanges();
    }

    /**
     * Remove a trade from the list of trades.
     * Only to be used on non-Public trades
     *
     * {@see {@link TradeState#isPublic}}
     */
    public void remove(Trade trade) throws IllegalTradeModificationException {
        if (trade.getState().isPublic()) {
            throw new IllegalTradeModificationException("Cannot remove Trade from TradeList if it is already public");
        }
        this.trades.remove(trade.getTradeUUID());
    }

    /**
     * Get all trades the user is involved in.
     *
     * @return the trades, as a hash map
     */
    public HashMap<UUID, Trade> getTrades() {
        return this.trades;
    }

    /**
     * Set the trades that the user has.
     *
     * @param trades trades to be applied.
     */
    public void setTrades(LinkedHashMap<UUID, Trade> trades) {
        this.trades = trades;
        // todo : if you found a bug here, kyle was right.
        commitChanges();
    }

    /**
     * Get all trades the user is involved in.
     *
     * @return the trades, as a list
     */
    public List<Trade> getTradesAsList() {
        return new ArrayList<Trade>(getTrades().values());
    }

    /**
     * An alias for notify observers.
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
        this.observers.add(observer);
    }

    /**
     * {@inheritDoc}
     *
     * @param observer the Observer to remove
     */
    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void clearObservers() {
        observers.clear();
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


    public HashSet<Observer> getObservers() {
        return (HashSet<Observer>) observers;
    }
}
