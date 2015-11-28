package ca.ualberta.cmput301.t03.datamanager;

import junit.framework.TestCase;

import java.util.ArrayList;

import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.FieldGroupedQuery;
import ca.ualberta.cmput301.t03.trading.TradeStateAccepted;
import ca.ualberta.cmput301.t03.trading.TradeStateOffered;

/**
 * Created by rishi on 15-11-27.
 */
public class FieldGroupedQueryTests extends TestCase {

    public void testFormQuery() {
        FieldGroupedQuery query = new FieldGroupedQuery("state", new ArrayList<String>() {
            {
                add(TradeStateOffered.stateString);
                add(TradeStateAccepted.stateString);
            }
        }, "owner.username", 5);

        String queryJson = query.formQuery();

        JsonFormatter formatter = new JsonFormatter(false, true);
        FieldGroupedQuery query2 = formatter.getGson().fromJson(queryJson, FieldGroupedQuery.class);

        assertEquals(query, query2);
    }
}
