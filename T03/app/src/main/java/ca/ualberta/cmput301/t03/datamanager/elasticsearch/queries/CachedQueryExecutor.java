package ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries;

import java.io.IOException;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.CachedDataManager;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.LocalDataManager;

/**
 * Created by rishi on 15-11-28.
 */
public class CachedQueryExecutor implements QueryExecutor {

    private final QueryExecutor innerExecutor;
    private final LocalDataManager cachingDataManager;

    public CachedQueryExecutor(QueryExecutor innerExecutor) {
        this.innerExecutor = Preconditions.checkNotNull(innerExecutor, "innerExecutor");
        this.cachingDataManager = new LocalDataManager();
    }

    @Override
    public QueryResult executeQuery(String suffix, Query query) throws IOException {
        QueryResult queryResult;

        try {
            queryResult = innerExecutor.executeQuery(suffix, query);
            writeToCache(suffix, query, queryResult);
        }
        catch (IOException e) {
            if (isQueryResultInCache(suffix, query)) {
                queryResult = getResultFromCache(suffix, query);
            }
            else {
                throw e;
            }
        }

        return queryResult;
    }

    private void writeToCache(String suffix, Query query, QueryResult queryResult) {
        cachingDataManager.writeData(getQueryDataKey(suffix, query), queryResult, QueryResult.class);
    }

    private QueryResult getResultFromCache(String suffix, Query query) throws IOException {
        return cachingDataManager.getData(getQueryDataKey(suffix, query), QueryResult.class);
    }

    private boolean isQueryResultInCache(String suffix, Query query) {
        return cachingDataManager.keyExists(getQueryDataKey(suffix, query));
    }

    private DataKey getQueryDataKey(String suffix, Query query) {
        return new DataKey(CachedDataManager.CACHE_DIRECTORY + "/" + suffix, query.getUniqueId());
    }
}
