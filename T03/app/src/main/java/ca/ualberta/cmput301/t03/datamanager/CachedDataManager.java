package ca.ualberta.cmput301.t03.datamanager;

import android.content.Context;

import java.io.IOException;
import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;

/**
 * Created by rishi on 15-10-31.
 */
public class CachedDataManager extends JsonDataManager {

    private final static String CACHE_DIRECTORY = "cache";
    private final JsonDataManager innerManager;
    private final Context context;
    private final LocalDataManager cachingDataManager;

    public CachedDataManager(JsonDataManager innerManager, Context context, boolean useExplicitExposeAnnotation) {
        this.innerManager = Preconditions.checkNotNull(innerManager, "innerManager");
        this.context = Preconditions.checkNotNull(context, "context");
        this.cachingDataManager = new LocalDataManager(context, useExplicitExposeAnnotation);
    }

    public CachedDataManager(JsonDataManager innerManager, Context context) {
        this(innerManager, context, innerManager.jsonFormatter.getUseExplicitExposeAnnotation());
    }

    @Override
    public boolean keyExists(DataKey key) throws IOException {
        if (innerManager.isOperational()) {
            return innerManager.keyExists(key);
        }

        return cachingDataManager.keyExists(convertToCacheKey(key));
    }

    @Override
    public <T> T getData(DataKey key, Type typeOfT) throws IOException {
        if (innerManager.isOperational()) {
            T retrievedData = innerManager.getData(key, typeOfT);
            cachingDataManager.writeData(convertToCacheKey(key), retrievedData, typeOfT);
            return retrievedData;
        }

        return cachingDataManager.getData(convertToCacheKey(key), typeOfT);
    }

    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException {
        if (isOperational()) {
            innerManager.writeData(key, obj, typeOfT);
            cachingDataManager.writeData(convertToCacheKey(key), obj, typeOfT);
        }
        else {
            throw new ServiceNotAvailableException("Inner DataManager is not operational. Cannot perform the write operation.");
        }
    }

    @Override
    public boolean deleteIfExists(DataKey key) throws IOException {
        if (isOperational()) {
            boolean existed = innerManager.deleteIfExists(key);
            cachingDataManager.deleteIfExists(convertToCacheKey(key));
            return existed;
        }
        throw new ServiceNotAvailableException("Inner DataManager is not operational. Cannot perform the delete operation.");
    }

    @Override
    public boolean isOperational() {
        return innerManager.isOperational();
    }

    private DataKey convertToCacheKey(DataKey key) {
        return new DataKey(CACHE_DIRECTORY + "/" + key.getType(), key.getId());
    }
}