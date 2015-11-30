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

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.JsonFormatter;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.ElasticSearchHelper;

/**
 * A {@link QueryExecutor} that performs HTTP requests to Elastic Search to get the results.
 * Created by rishi on 15-11-28.
 */
public class HttpQueryExecutor implements QueryExecutor {

    private final ElasticSearchHelper elasticSearchHelper = new ElasticSearchHelper();

    /**
     * {@inheritDoc}
     */
    @Override
    public AggregationQueryResult executeQuery(String suffix, Query query) throws IOException {
        Preconditions.checkNotNull(query, "query");
        Preconditions.checkNotNullOrWhitespace(suffix, "suffix");

        String resultJson =  elasticSearchHelper.postJson(query.formQuery(), suffix);
        return new JsonFormatter(false, true).getGson().fromJson(resultJson, AggregationQueryResult.class);
    }
}
