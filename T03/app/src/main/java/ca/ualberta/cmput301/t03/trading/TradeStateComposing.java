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

import java.io.IOException;

import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeModificationException;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;

/**
 * class TradeStateComposing implements {@link TradeState}
 */
public class TradeStateComposing implements TradeState {
    public final static String stateString = "TradeStateComposing";

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isClosed() {
        return Boolean.FALSE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isOpen() {
        return !isClosed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isEditable() {
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isPublic() {
        return Boolean.FALSE;
    }

    /**
     * {@inheritDoc}
     *
     * @param trade Trade to be offered.
     */
    @Override
    public void offer(Trade trade) throws ServiceNotAvailableException {
        trade.setState(new TradeStateOffered());
    }

    /**
     * {@inheritDoc}
     *
     * @param trade Trade to be cancelled.
     */
    @Override
    public void cancel(Trade trade) throws ServiceNotAvailableException {
        try {
            trade.getBorrower().getTradeList().remove(trade);
            trade.getOwner().getTradeList().remove(trade);
        } catch (IllegalTradeModificationException e) {
            Log.e("trade", e.getMessage());
        } catch (ServiceNotAvailableException e) {
            // todo snackbar toast saying we're offline?
        } catch (IOException e) {
            // todo handle this exception
        }
        trade.setState(new TradeStateCancelled());
    }

    /**
     * {@inheritDoc}
     *
     * @param trade Trade to be accepted.
     * @throws IllegalTradeStateTransition
     */
    @Override
    public void accept(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Trade being composed cannot be accepted");
    }

    /**
     * {@inheritDoc}
     *
     * @param trade Trade to be declined.
     * @throws IllegalTradeStateTransition
     */
    @Override
    public void decline(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Trade being composed cannot be declined");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInterfaceString(Boolean currentUserIsOwner) {
        return "COMPOSING-NOT-IN-INTERFACE";
    }

    @Override
    public String toString() {
        return stateString;
    }
}
