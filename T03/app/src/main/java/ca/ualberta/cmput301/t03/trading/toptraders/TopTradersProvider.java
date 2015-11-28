package ca.ualberta.cmput301.t03.trading.toptraders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.AggregationQueryResult;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.FieldGroupedQuery;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.Query;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.QueryExecutor;
import ca.ualberta.cmput301.t03.trading.TradeStateAccepted;
import ca.ualberta.cmput301.t03.trading.TradeStateOffered;

/**
 * Created by rishi on 15-11-28.
 */
public class TopTradersProvider {
    protected static final String queryID = "top_traders_query";

    private final QueryExecutor queryExecutor;

    public TopTradersProvider(QueryExecutor queryExecutor) {
        this.queryExecutor = Preconditions.checkNotNull(queryExecutor, "queryExecutor");
    }

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
