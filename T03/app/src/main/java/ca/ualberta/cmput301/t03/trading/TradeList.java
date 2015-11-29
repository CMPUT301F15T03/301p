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

import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.common.exceptions.ExceptionUtils;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.inventory.Adaptable;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeModificationException;

/**
 * TradeList represents a collection of trades a user is currently involved in. This is the model
 * for the TradeHistory workflow.
 */
public class TradeList implements Observable, Observer, Adaptable<Trade>, Iterable<Trade> {
    public static final String type = "TradeList";

    @Expose
    private List<Trade> trades;
    private Set<Observer> observers;

    public TradeList() {
        this.trades = new ArrayList<>();
        this.observers = new HashSet<>();
    }

    /**
     * Add a trade to the user.
     *
     * @param trade trade to be added
     */
    public void addTrade(Trade trade) {
        this.trades.add(trade);
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
        this.trades = new ArrayList<>();
        this.commitChanges();
    }

    /**
     * Remove a trade from the list of trades.
     * Only to be used on non-Public trades
     *
     * {@see {@link TradeState#isPublic}}
     */
    public void remove(Trade trade) throws IllegalTradeModificationException, ServiceNotAvailableException {
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
        HashMap<UUID, Trade> tr = new HashMap<>();
        for (Trade t: trades){
            tr.put(t.getTradeUUID(), t);
        }
        return tr;
    }

    public Trade getTrade(UUID uuid){
        return getTrades().get(uuid);
    }

    /**
     * Set the trades that the user has.
     *
     * @param trades trades to be applied.
     */
    public void setTrades(LinkedHashMap<UUID, Trade> trades) {
        this.trades = new ArrayList<Trade>(trades.values());
        // todo : if you found a bug here, kyle was right.
        commitChanges();
    }

    /**
     * Get all trades the user is involved in.
     *
     * @return the trades, as a list
     */
    public List<Trade> getTradesAsList() {
        return trades;
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

    @Override
    public List<Trade> getAdaptableItems() throws IOException, ServiceNotAvailableException {
        final ArrayList<Trade> list = new ArrayList<>();
        final TradeList tradeList = this;
        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Trade t: tradeList.getTrades().values()){
                    try {
                        if (t.isPublic()) {
                            list.add(t);
                        }
                    } catch (ServiceNotAvailableException e) {
                        ExceptionUtils.toastErrorWithNetwork();
                    }
                }
            }
        });
        worker.start();
        try {
            worker.join();
        } catch (InterruptedException e) {
            Log.e("TradeList", "getAdaptableItems failed, thread was interrupted while running");
        }

        Collections.sort(list);

        return list;
    }

    @Override
    public Iterator<Trade> iterator() {
        return trades.iterator();
    }
}
