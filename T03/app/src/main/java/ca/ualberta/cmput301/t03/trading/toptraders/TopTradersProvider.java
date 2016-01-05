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

package ca.ualberta.cmput301.t03.trading.toptraders;

import com.udeyrishi.androidelasticsearchdatamanager.Preconditions;
import com.udeyrishi.androidelasticsearchdatamanager.queries.AggregationQueryResult;
import com.udeyrishi.androidelasticsearchdatamanager.queries.FieldGroupedQuery;
import com.udeyrishi.androidelasticsearchdatamanager.queries.Query;
import com.udeyrishi.androidelasticsearchdatamanager.queries.QueryExecutor;

import java.io.IOException;
import java.util.ArrayList;

import ca.ualberta.cmput301.t03.trading.TradeStateAccepted;
import ca.ualberta.cmput301.t03.trading.TradeStateOffered;

/**
 * Uses a {@link QueryExecutor} to get the list of top traders. Acts as a refresh controller for the
 * {@link TopTradersFragment}.
 * Created by rishi on 15-11-28.
 */
public class TopTradersProvider {
    protected static final String queryID = "top_traders_query";

    private final QueryExecutor queryExecutor;

    /**
     * Creates a new instance of {@link TopTradersProvider}. Uses the {@link QueryExecutor} provided
     * for executing a {@link FieldGroupedQuery} query that gets the list of top traders. The top
     * trader list is created on the basis on the maximum number of successful trades (== completed
     * and ongoing).
     * @param queryExecutor The {@link QueryExecutor} to be used for executing queries.
     */
    public TopTradersProvider(QueryExecutor queryExecutor) {
        this.queryExecutor = Preconditions.checkNotNull(queryExecutor, "queryExecutor");
    }

    /**
     * Uses the provided {@link QueryExecutor} to get the list of the current top traders.
     * @param count The maximum number of results to be retrieved.
     * @return The list of the current top traders.
     * @throws IOException Thrown, if the{@link QueryExecutor#executeQuery(String, Query)} throws it.
     */
    public ArrayList<TopTrader> getTopTraders(int count) throws IOException {
        Query query = new FieldGroupedQuery("state",
            new ArrayList<String>() { { add(TradeStateOffered.stateString); add(TradeStateAccepted.stateString); } },
        "owner.username", count, queryID);

        AggregationQueryResult result = queryExecutor.executeQuery("Trade/_search", query);
        ArrayList<AggregationQueryResult.Bucket> buckets = result.getAggregations().getGroup().getBuckets();

        ArrayList<TopTrader> topTraders = new ArrayList<>();

        for (AggregationQueryResult.Bucket bucket : buckets) {
            topTraders.add(new TopTrader(bucket.getKey(), bucket.getCount()));
        }

        return topTraders;
    }
}
