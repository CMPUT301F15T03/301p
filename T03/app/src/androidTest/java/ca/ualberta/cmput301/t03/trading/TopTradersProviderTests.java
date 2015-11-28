package ca.ualberta.cmput301.t03.trading;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.AggregationQueryResult;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.CachedQueryExecutor;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.FieldGroupedQuery;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.HttpQueryExecutor;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.Query;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.QueryExecutor;

/**
 * Created by rishi on 15-11-28.
 */
public class TopTradersProviderTests extends TestCase {

    public void testProvidesTopTraders() throws IOException {
        TopTradersProvider topTradersProvider = new TopTradersProvider(new CachedQueryExecutor(new HttpQueryExecutor()));
        HashMap<String, Integer> topTraders = topTradersProvider.getTopTraders(5);

        assertNotNull(topTraders);
        assertTrue(topTraders.size() <= 5);

        for (String key : topTraders.keySet()) {
            assertFalse(key.trim().isEmpty());
            assertNotNull(topTraders.get(key));
        }
    }
}
