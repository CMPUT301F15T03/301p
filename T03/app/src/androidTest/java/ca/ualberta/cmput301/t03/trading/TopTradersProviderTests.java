package ca.ualberta.cmput301.t03.trading;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.List;

import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.CachedQueryExecutor;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.HttpQueryExecutor;
import ca.ualberta.cmput301.t03.trading.toptraders.TopTrader;
import ca.ualberta.cmput301.t03.trading.toptraders.TopTradersProvider;

/**
 * Created by rishi on 15-11-28.
 */
public class TopTradersProviderTests extends TestCase {

    public void testProvidesTopTraders() throws IOException {
        TopTradersProvider topTradersProvider = new TopTradersProvider(new CachedQueryExecutor(new HttpQueryExecutor()));
        List<TopTrader> topTraders = topTradersProvider.getTopTraders(5);

        assertNotNull(topTraders);
        assertTrue(topTraders.size() <= 5);

        for (TopTrader trader : topTraders) {
            assertNotNull(trader.getUserName());
            assertFalse(trader.getUserName().trim().isEmpty());
            assertNotNull(trader.getSuccessfulTradeCount());
        }
    }
}
