package ca.ualberta.cmput301.t03.trading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.AggregationQueryResult;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.FieldGroupedQuery;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.Query;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.QueryExecutor;

/**
 * Created by rishi on 15-11-28.
 */
public class TopTradersProvider {
    private static final String queryID = "top_traders_query";

    private final QueryExecutor queryExecutor;

    public TopTradersProvider(QueryExecutor queryExecutor) {
        this.queryExecutor = Preconditions.checkNotNull(queryExecutor, "queryExecutor");
    }

    public HashMap<String, Integer> getTopTraders(int count) throws IOException {
        Query query = new FieldGroupedQuery("state",
            new ArrayList<String>() { { add(TradeStateOffered.stateString); add(TradeStateAccepted.stateString); } },
        "owner.username", count, queryID);

        AggregationQueryResult result = queryExecutor.executeQuery("Trade/_search", query);
        ArrayList<AggregationQueryResult.Bucket> buckets = result.getAggregations().getGroup().getBuckets();

        HashMap<String, Integer> topTraders = new HashMap<>();

        for (AggregationQueryResult.Bucket bucket : buckets) {
            topTraders.put(bucket.getKey(), bucket.getCount());
        }

        return topTraders;
    }
}
