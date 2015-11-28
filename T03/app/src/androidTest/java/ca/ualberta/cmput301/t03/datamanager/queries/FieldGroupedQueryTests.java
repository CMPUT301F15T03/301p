package ca.ualberta.cmput301.t03.datamanager.queries;

import junit.framework.TestCase;

import java.util.ArrayList;

import ca.ualberta.cmput301.t03.datamanager.JsonFormatter;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.FieldGroupedQuery;
import ca.ualberta.cmput301.t03.trading.TradeStateAccepted;
import ca.ualberta.cmput301.t03.trading.TradeStateOffered;

/**
 * Created by rishi on 15-11-27.
 */
public class FieldGroupedQueryTests extends TestCase {

    private static final String expectedQueryJson = "{\n" +
            "  \"aggs\": {\n" +
            "    \"group_by_owner_username\": {\n" +
            "      \"terms\": {\n" +
            "        \"field\": \"owner.username\",\n" +
            "        \"size\": 5\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"query\": {\n" +
            "    \"query_string\": {\n" +
            "      \"default_field\": \"state\",\n" +
            "      \"query\": \"TradeStateOffered OR TradeStateAccepted\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public void testFormQuery() {
        FieldGroupedQuery query = new FieldGroupedQuery("state", new ArrayList<String>() {
            {
                add(TradeStateOffered.stateString);
                add(TradeStateAccepted.stateString);
            }
        }, "owner.username", 5, "test_query");

        String queryJson = query.formQuery();
        assertEquals(expectedQueryJson, queryJson);

        JsonFormatter formatter = new JsonFormatter(false, true);
        FieldGroupedQuery query2 = formatter.getGson().fromJson(queryJson, FieldGroupedQuery.class);

        assertEquals(query, query2);
    }
}
