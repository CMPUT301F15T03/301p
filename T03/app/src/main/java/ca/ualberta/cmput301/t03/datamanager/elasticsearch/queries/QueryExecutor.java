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

import java.io.IOException;

/**
 * An interface for a class that executes {@link Query}
 * Created by rishi on 15-11-28.
 */
public interface QueryExecutor {

    /**
     * Performs the query and gets the {@link AggregationQueryResult} obtained back.
     * @param suffix The suffix at which the query should be executed.
     * @param query The {@link Query} to be executed.
     * @return The obtained {@link AggregationQueryResult}.
     * @throws IOException Thrown, if there is some network failure.
     */
    AggregationQueryResult executeQuery(String suffix, Query query) throws IOException;
}
