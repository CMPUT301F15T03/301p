package ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries;

import java.io.IOException;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.JsonFormatter;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.ElasticSearchHelper;

/**
 * Created by rishi on 15-11-28.
 */
public class HttpQueryExecutor implements QueryExecutor {

    private final ElasticSearchHelper elasticSearchHelper = new ElasticSearchHelper();

    @Override
    public AggregationQueryResult executeQuery(String suffix, Query query) throws IOException {
        Preconditions.checkNotNull(query, "query");
        Preconditions.checkNotNullOrWhitespace(suffix, "suffix");

        String resultJson =  elasticSearchHelper.postJson(query.formQuery(), suffix);
        return new JsonFormatter(false, true).getGson().fromJson(resultJson, AggregationQueryResult.class);
    }
}
