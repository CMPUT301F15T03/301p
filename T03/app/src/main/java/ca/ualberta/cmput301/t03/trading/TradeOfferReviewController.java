/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
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

import java.util.UUID;

import ca.ualberta.cmput301.t03.common.exceptions.ExceptionUtils;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;

/**
 * Controller for reviewing a trade which has been offered to the user.
 * <p>
 * The current user is able to review trades which have been offered to them.
 * Available actions are accepting, declining, and declining-and-counter-offering.
 * <p>
 * Any given instance of TradeOfferReviewController is owned by a {@link TradeOfferReviewActivity}.
 */
public class TradeOfferReviewController {
    private final String logTAG = "TradeOfferReview";

    private Context context;
    private Trade model;

    public TradeOfferReviewController(Context context, Trade model) {
        this.context = context;
        this.model = model;
    }

    /**
     * Accept the trade.
     * <p>
     * The IllegalTradeStateTransition should never be thrown/caught. If the user
     * is in TradeOfferReviewActivity, they are reviewing a trade which is in
     * {@link TradeStateOffered}. {@link TradeState#accept(Trade)} is a legal action.
     */
    public void acceptTrade() throws ServiceNotAvailableException {
        try {
            model.accept();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            ExceptionUtils.toastLong(illegalTradeStateTransition.getMessage());
        }
    }

    /**
     * Complete the trade.
     */
    public void completeTrade() throws ServiceNotAvailableException {
        try {
            model.complete();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            ExceptionUtils.toastLong(illegalTradeStateTransition.getMessage());
        }
    }

    /**
     * Decline the trade.
     * <p>
     * The IllegalTradeStateTransition should never be thrown/caught. If the user
     * is in TradeOfferReviewActivity, they are reviewing a trade which is in
     * {@link TradeStateOffered}. Thus, {@link TradeState#decline(Trade)} is a legal action.
     */
    public void declineTrade() throws ServiceNotAvailableException {
        try {
            model.decline();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            ExceptionUtils.toastLong(illegalTradeStateTransition.getMessage());
        }
    }

    /**
     * Decline the trade, and prepare to compose a new Counter-Trade
     * <p>
     * The IllegalTradeStateTransition should never be thrown/caught. If the user
     * is in TradeOfferReviewActivity, they are reviewing a trade which is in
     * {@link TradeStateOffered}. Thus, {@link TradeState#decline(Trade)} is a legal action.
     */
    public void declineAndCounterTrade() throws ServiceNotAvailableException {
        try {
            model.decline();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            ExceptionUtils.toastLong(illegalTradeStateTransition.getMessage());
        }
        /**
         * TODO perform required tasks for counter offer
         * - create a new trade
         * - send to compose trade view
         */

        Trade t = new Trade(UUID.randomUUID(), context);

        throw new NotImplementedException();
    }
}