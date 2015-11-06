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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;

/**
 * Created by ross on 15-11-05.
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

    public void addTrade(Trade trade) {
        this.trades.put(trade.getTradeUUID(), trade);
        commitChanges();
    }

    public void setTrades(LinkedHashMap<UUID, Trade> trades) {
        this.trades = trades;
        commitChanges();
    }

    public HashMap<UUID, Trade> getTrades() {
        return this.trades;
    }

    public void commitChanges() {
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void update(Observable observable) {
        notifyObservers();
    }
}
