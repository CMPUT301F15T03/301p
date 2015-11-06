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

import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;

/**
 * class TradeState uses the State Pattern to manage the various states a {@link Trade}
 * can be in.
 *
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">Wikipedia: State Pattern</a>
 */
public interface TradeState {
    /**
     * Returns whether the TradeState is Closed.
     *
     * A Closed trade is one which has been accepted or declined.
     * That is, a Closed trade can no longer be interacted with.
     *
     * @return True if the trade is Closed. Returns false otherwise.
     */
    Boolean isClosed();

    /**
     * Returns whether the TradeState is Open.
     *
     * An open trade is one which has been neither accepted nor declined.
     * That is, an open trade can be interacted with in some way.
     *
     * @return True if the trade is Open. Returns false otherwise.
     */
    Boolean isOpen();

    /**
     * Returns whether the TradeState is Editable.
     *
     * An editable trade is one which has not yet been offered.
     *
     * @return True if the trade is Editable. Returns false otherwise.
     */
    Boolean isEditable();

    /**
     * Offer a trade.
     *
     * A trade can only be offered if it is currently being composed, that is,
     * if the current TradeState is {@link TradeStateComposing}.
     *
     * @param trade Trade to be offered.
     * @throws IllegalTradeStateTransition if the trade is in an illegal state and cannot be offered.
     */
    void offer(Trade trade) throws IllegalTradeStateTransition;

    /**
     * Cancel a trade.
     *
     * A trade can only be cancelled if it is currently being composed, that is,
     * if the current TradeState is {@link TradeStateComposing}.
     *
     * @param trade Trade to be cancelled.
     * @throws IllegalTradeStateTransition if the trade is in an illegal state and cannot be cancelled.
     */
    void cancel(Trade trade) throws IllegalTradeStateTransition;

    /**
     * Accept a trade.
     *
     * A trade can only be accepted if it has been offered, that is,
     * if the current TradeState is {@link TradeStateOffered}.
     *
     * @param trade Trade to be accepted.
     * @throws IllegalTradeStateTransition if the trade is in an illegal state and cannot be accepted.
     */
    void accept(Trade trade) throws IllegalTradeStateTransition;

    /**
     * Decline a trade.
     *
     * A trade can only be declined if it has been offered, that is,
     * if the current TradeState is {@link TradeStateOffered}.
     *
     * @param trade Trade to be declined.
     * @throws IllegalTradeStateTransition if the trade is in an illegal state and cannot be declined.
     */
    void decline(Trade trade) throws IllegalTradeStateTransition;
}
