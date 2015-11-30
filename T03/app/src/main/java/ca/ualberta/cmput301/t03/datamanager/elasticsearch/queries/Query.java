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

package ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries;

/**
 * An interface for an Elastic Search query.
 * Created by rishi on 15-11-27.
 */
public interface Query {

    /**
     * The key for the aggregations results in the query response.
     */
    String AGGREGATION_KEY = "group";

    /**
     * Form a string query that can be sent as the content of the query HTTP request.
     * @return The JSON query.
     */
    String formQuery();

    /**
     * Get a unique ID for this query. Can be used to cache results for this query.
     * @return The unique ID for this query.
     */
    String getUniqueId();
}
