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

package ca.ualberta.cmput301.t03.user;

/**
 * Controls searching for friends via a query
 */
public class FriendsSearchController {
    private FriendsList friendsList;

    /**
     * Sets the query to search for friends by. Use to filter the global friends list by a query.
     *
     * @param query The username to query
     */
    public void setQuery(String query) {
        throw new UnsupportedOperationException();
    }

    /**
     * Clears the query to search for friends by. This resets the query to nil, ie there will
     * be no filter on the search results.
     */
    public void clearQuery() {
        throw new UnsupportedOperationException();
    }
}
