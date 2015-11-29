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

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeModificationException;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;

/**
 * The controller portion of the TradeOfferCompose triplet. This will modify the Trade model upon
 * request of the view.
 */
public class TradeOfferComposeController {
    private final String logTAG = "TradeOfferCompose";
    private Context context;

    private Trade model;

    public TradeOfferComposeController(Context context, Trade model) {
        this.model = model;
        this.context = context;
    }

    /**
     * Trigger an offer on the model, this may or may not transition based on the state of the
     * model.
     */
    public void offerTrade() throws ServiceNotAvailableException {
        try {
            model.offer();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            Log.e(logTAG, illegalTradeStateTransition.getMessage());
        }
    }

    /**
     * Trigger a cancel trade on the model, this may or may not transition based on the state of the
     * model.
     */
    public void cancelTrade() throws ServiceNotAvailableException {
        try {
            model.cancel();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            Log.e(logTAG, illegalTradeStateTransition.getMessage());
        }
    }

    /**
     * add an item to the borrower section of the trade barter.
     *
     * @param item item to be added
     */
    public void addBorrowerItem(Item item) throws ServiceNotAvailableException, IllegalTradeModificationException {
        List<Item> items = model.getBorrowersItems();
        items.add(item);
        model.setBorrowersItems(items);
    }
}
