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
import android.util.Log;

import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;

/**
 * Created by ross on 15-10-29.
 */
public class TradeOfferComposeController {
    private final String logTAG = "TradeOfferCompose";
    private Context context;

    private Trade model;

    public TradeOfferComposeController(Context context, Trade model) {
        this.model = model;
        this.context = context;
    }

    public void offerTrade() {
        try {
            model.offer();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            Log.e(logTAG, illegalTradeStateTransition.getMessage());
        }
    }

    public void cancelTrade() {
        try {
            model.cancel();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            Log.e(logTAG, illegalTradeStateTransition.getMessage());
        }
    }

    public void addBorrowerItem(Item item) {
        model = new Trade(model.getBorrower(), model.getOwner(),
                model.getBorrowersItems(), model.getOwnersItems(), this.context);
    }
}
