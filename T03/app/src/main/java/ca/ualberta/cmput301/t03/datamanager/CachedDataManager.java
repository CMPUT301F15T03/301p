/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of {ApplicationName}
 *
 * {ApplicationName} is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.ualberta.cmput301.t03.datamanager;

import android.content.Context;

import java.io.IOException;
import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;

/**
 * A {@link JsonDataManager} that wraps around an inner {@link JsonDataManager} to keep a copy of
 * the data in a local cache using an {@link LocalDataManager}. Allows to perform some operations
 * when the inner manager's {@link JsonDataManager#isOperational()} returns false.
 * <p>
 * <p>
 * Allows to perform the
 * {@link DataManager#keyExists(DataKey)} and {@link DataManager#getData(DataKey, Type)} operations
 * when the innerManager's {@link JsonDataManager#isOperational()} returns false. In that case,
 * these operations will be performed on the local cache.
 * Created by rishi on 15-10-31.
 */
public class CachedDataManager extends JsonDataManager {

    private final static String CACHE_DIRECTORY = "cache";
    private final LocalDataManager cachingDataManager;
    protected final JsonDataManager innerManager;

    /**
     * Creates an instance of the {@link CachedDataManager}.
     *
     * @param innerManager                The inner {@link JsonDataManager} manager to be used.
     * @param context                     The {@link Context} to be used for accessing the local filesystem for caching.
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public CachedDataManager(JsonDataManager innerManager, Context context, boolean useExplicitExposeAnnotation) {
        Preconditions.checkNotNull(context, "context");
        this.innerManager = Preconditions.checkNotNull(innerManager, "innerManager");
        this.cachingDataManager = new LocalDataManager(context, useExplicitExposeAnnotation);
    }

    /**
     * Creates an instance of the {@link CachedDataManager}. The manager will set the "useExplicitExposeAnnotation"
     * value to false.
     *
     * @param innerManager The inner {@link JsonDataManager} manager to be used.
     * @param context      The {@link Context} to be used for accessing the local filesystem for caching.
     */
    public CachedDataManager(JsonDataManager innerManager, Context context) {
        this(innerManager, context, innerManager.jsonFormatter.getUseExplicitExposeAnnotation());
    }

    /**
     * If the inner manager is operational, then checks if the {@link DataKey} exists in the inner
     * manager. Else, checks the cache.
     *
     * @param key The {@link DataKey} to be looked up.
     * @return True, if the key is found, else false.
     * @throws IOException Thrown, if the communications fails with the storage media being used.
     */
    @Override
    public boolean keyExists(DataKey key) throws IOException {
        if (innerManager.isOperational()) {
            return innerManager.keyExists(key);
        }

        return cachingDataManager.keyExists(convertToCacheKey(key));
    }

    /**
     * If the inner manager is operational, then calls {@link JsonDataManager#getData(DataKey, Type)}
     * on it, else calls it on the cache.
     *
     * @param key     The {@link DataKey} to be used for lookup.
     * @param typeOfT The {@link Type} of the object being retrieved.
     * @param <T>     The type of the object being retrieved. This type should match with the parameter
     *                "typeOfT", else a {@link ClassCastException} might be thrown.
     * @return The retrieved object.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    @Override
    public <T> T getData(DataKey key, Type typeOfT) throws IOException, DataKeyNotFoundException, ClassCastException {
        if (innerManager.isOperational()) {
            T retrievedData = innerManager.getData(key, typeOfT);
            cachingDataManager.writeData(convertToCacheKey(key), retrievedData, typeOfT);
            return retrievedData;
        }

        return cachingDataManager.getData(convertToCacheKey(key), typeOfT);
    }

    /**
     * Writes the object to the inner manager, if it is operational.
     *
     * @param key     The {@link DataKey} for the object.
     * @param obj     The object to be stored.
     * @param typeOfT The {@link Type} of the object.
     * @param <T>     The type of the object.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException {
        if (isOperational()) {
            innerManager.writeData(key, obj, typeOfT);
            writeToCache(key, obj, typeOfT);
        } else {
            throw new ServiceNotAvailableException("Inner DataManager is not operational. Cannot perform the write operation.");
        }
    }

    /**
     * Deletes the object from the inner manager and the cache, if the inner manager is operational.
     * Only deletes if the key is found.
     *
     * @param key The {@link DataKey} for which the object has to be deleted.
     * @return True, if the key is found and deleted. False, if the key is not found.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    @Override
    public boolean deleteIfExists(DataKey key) throws IOException {
        if (isOperational()) {
            boolean existed = innerManager.deleteIfExists(key);
            deleteFromCache(key);
            return existed;
        }
        throw new ServiceNotAvailableException("Inner DataManager is not operational. Cannot perform the delete operation.");
    }

    /**
     * True, if the inner manager is operational, else false. In this case, isOperational == false
     * implies that only the write and delete operations are always completely non-operational.
     * The get and keyExists operations may still operate (partially) on the cache.
     *
     * @return True, if the inner manager is operational, else false.
     */
    @Override
    public boolean isOperational() {
        return innerManager.isOperational();
    }

    /**
     * Returns if the innerManager requires network or not.
     */
    @Override
    public boolean requiresNetwork() {
        return innerManager.requiresNetwork();
    }

    /**
     * Writes the object to the cache.
     * @param key The {@link DataKey} for the object that was passed. This will be converted to an
     *            appropriate caching key.
     * @param obj The object to be cached.
     * @param typeOfT The {@link Type} of the object.
     * @param <T> The type of the object.
     */
    protected <T> void writeToCache(DataKey key, T obj, Type typeOfT) {
        DataKey cacheKey = convertToCacheKey(key);
        cachingDataManager.writeData(cacheKey, obj, typeOfT);
    }

    /**
     * Deletes the object from the cache, if it exists.
     * @param key The {@link DataKey} for the object that was passed. This will be converted to an
     *            appropriate caching key.
     * @return True, if the key was found in the cache and was deleted. False, if the key was not
     *         found.
     */
    protected boolean deleteFromCache(DataKey key) {
        DataKey cacheKey = convertToCacheKey(key);
        return cachingDataManager.deleteIfExists(cacheKey);
    }

    private DataKey convertToCacheKey(DataKey key) {
        return new DataKey(CACHE_DIRECTORY + "/" + key.getType(), key.getId());
    }
}