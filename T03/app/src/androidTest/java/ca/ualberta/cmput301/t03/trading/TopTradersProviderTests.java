package ca.ualberta.cmput301.t03.trading;

import com.udeyrishi.androidelasticsearchdatamanager.elasticsearchhelpers.ElasticSearchHelper;
import com.udeyrishi.androidelasticsearchdatamanager.queries.CachedQueryExecutor;
import com.udeyrishi.androidelasticsearchdatamanager.queries.HttpQueryExecutor;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.List;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.trading.toptraders.TopTrader;
import ca.ualberta.cmput301.t03.trading.toptraders.TopTradersProvider;

/**
 * Created by rishi on 15-11-28.
 */
public class TopTradersProviderTests extends TestCase {

    public void testProvidesTopTraders() throws IOException {
        TopTradersProvider topTradersProvider = new TopTradersProvider(new CachedQueryExecutor(TradeApp.getContext(),
                new HttpQueryExecutor(new ElasticSearchHelper(TradeApp.getContext().getString(R.string.elasticSearchRootUrl)))));
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
