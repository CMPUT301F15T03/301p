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

import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;

/**
 * class TradeStateDeclined implements {@link TradeState}
 */
public class TradeStateDeclined implements TradeState {
    public final static String stateString = "TradeStateDeclined";

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isClosed() {
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isPending() {
        return !isClosed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isEditable() {
        return Boolean.FALSE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isPublic() {
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     *
     * @param trade Trade to be offered.
     * @throws IllegalTradeStateTransition
     */
    @Override
    public void offer(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Declined trade cannot be offered");
    }

    /**
     * {@inheritDoc}
     *
     * @param trade Trade to be cancelled.
     * @throws IllegalTradeStateTransition
     */
    @Override
    public void cancel(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Declined trade cannot be cancelled");
    }

    /**
     * {@inheritDoc}
     *
     * @param trade Trade to be accepted.
     * @throws IllegalTradeStateTransition
     */
    @Override
    public void accept(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Declined trade cannot be accepted");
    }

    /**
     * {@inheritDoc}
     *
     * @param trade Trade to be completed.
     * @throws IllegalTradeStateTransition
     */
    @Override
    public void complete(Trade trade) throws IllegalTradeStateTransition, ServiceNotAvailableException {
        throw new IllegalTradeStateTransition("Declined trade cannot be completed");
    }

    /**
     * {@inheritDoc}
     *
     * @param trade Trade to be declined.
     * @throws IllegalTradeStateTransition
     */
    @Override
    public void decline(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Declined trade cannot be declined");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInterfaceString(Boolean currentUserIsOwner) {
        if (currentUserIsOwner) {
            return "Declined, not traded to";
        } else {
            return "Declined, not received from";
        }
    }

    @Override
    public String getStateString() {
        return "Declined";
    }

    @Override
    public String toString() {
        return stateString;
    }

}
