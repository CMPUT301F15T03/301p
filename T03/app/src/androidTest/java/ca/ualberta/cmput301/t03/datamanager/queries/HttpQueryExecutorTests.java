package ca.ualberta.cmput301.t03.datamanager.queries;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.AggregationQueryResult;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.FieldGroupedQuery;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.HttpQueryExecutor;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.Query;
import ca.ualberta.cmput301.t03.trading.TradeStateAccepted;
import ca.ualberta.cmput301.t03.trading.TradeStateOffered;

/**
 * Created by rishi on 15-11-28.
 */
public class HttpQueryExecutorTests extends TestCase {

    public void testExecuteQuery() throws IOException {
        HttpQueryExecutor queryExecutor = new HttpQueryExecutor();

        Query query = new FieldGroupedQuery("state",
                new ArrayList<String>() { { add(TradeStateOffered.stateString); add(TradeStateAccepted.stateString); } },
                "owner.username", 5, "top_traders_query");
        AggregationQueryResult result = queryExecutor.executeQuery("Trade/_search", query);

        AggregationQueryResult.Aggregations aggregations = result.getAggregations();
        assertNotNull(aggregations);

        AggregationQueryResult.AggregationGroup group = aggregations.getGroup();
        assertNotNull(group);

        List<AggregationQueryResult.Bucket> buckets = group.getBuckets();
        assertNotNull(buckets);

        assertNotNull(buckets);
        assertTrue(buckets.size() > 0);

        for (AggregationQueryResult.Bucket bucket : buckets) {
            assertTrue(bucket.getCount() > 0);
            assertNotNull(bucket.getKey());
            assertFalse(bucket.getKey().trim().isEmpty());
        }

    }
}
