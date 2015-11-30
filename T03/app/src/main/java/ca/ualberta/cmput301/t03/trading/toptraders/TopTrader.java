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

package ca.ualberta.cmput301.t03.trading.toptraders;

/**
 * The model modelling a Top Trader (or just a simple trader) containing the trader's username
 * and his successful trade count.
 * Created by rishi on 15-11-28.
 */
public class TopTrader {
    private final String userName;
    private final Integer successfulTradeCount;

    /**
     * Creates a new instance of TopTrader with a given username and his successful trade count.
     * @param userName The username of the trader.
     * @param successfulTradeCount The successful trade count of this username.
     */
    public TopTrader(String userName, Integer successfulTradeCount) {
        this.userName = userName;
        this.successfulTradeCount = successfulTradeCount;
    }

    /**
     * Returns the top trader's successful trade count.
     * @return The top trader's successful trade count.
     */
    public Integer getSuccessfulTradeCount() {
        return successfulTradeCount;
    }

    /**
     * Gets the top trader's user name.
     * @return The top trader's user name.
     */
    public String getUserName() {
        return userName;
    }
}
