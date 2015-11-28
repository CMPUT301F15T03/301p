package ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries;

import java.io.IOException;

/**
 * Created by rishi on 15-11-28.
 */
public interface QueryExecutor {
    QueryResult executeQuery(String suffix, Query query) throws IOException;
}
