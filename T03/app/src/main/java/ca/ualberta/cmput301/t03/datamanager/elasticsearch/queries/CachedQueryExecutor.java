package ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries;

import java.io.IOException;
import java.lang.reflect.Type;

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
    public <T> QueryResult<T> executeQuery(String suffix, Query query, Type typeOfQueryResult) throws IOException {
        QueryResult<T> queryResult;

        try {
            queryResult = innerExecutor.executeQuery(suffix, query, typeOfQueryResult);
            writeToCache(suffix, query, queryResult);
        }
        catch (IOException e) {
            if (isQueryResultInCache(suffix, query)) {
                queryResult = getResultFromCache(suffix, query, typeOfQueryResult);
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

    private <T> QueryResult<T> getResultFromCache(String suffix, Query query, Type typeOfQueryResult) throws IOException {
        return cachingDataManager.getData(getQueryDataKey(suffix, query), typeOfQueryResult);
    }

    private boolean isQueryResultInCache(String suffix, Query query) {
        return cachingDataManager.keyExists(getQueryDataKey(suffix, query));
    }

    private DataKey getQueryDataKey(String suffix, Query query) {
        return new DataKey(CachedDataManager.CACHE_DIRECTORY + "/" + suffix, query.getUniqueId());
    }
}
