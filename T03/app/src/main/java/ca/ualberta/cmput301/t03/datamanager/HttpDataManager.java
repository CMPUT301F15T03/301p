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

import android.content.res.Resources;

import com.path.android.jobqueue.network.NetworkUtil;

import java.io.IOException;
import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.ElasticSearchHelper;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.ElasticSearchNetworkUtil;

/**
 * A {@link JsonDataManager} that uses the ElasticSearch server as the storage media.
 * Created by rishi on 15-10-30.
 */
public class HttpDataManager extends JsonDataManager {
    private final ElasticSearchHelper elasticSearchHelper;
    private final NetworkUtil networkUtil;

    /**
     * Creates an instance of {@link HttpDataManager}.
     *
     * @param networkUtil The {@link NetworkUtil} to be used for checking the network state.
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public HttpDataManager(NetworkUtil networkUtil, boolean useExplicitExposeAnnotation) {
        super(useExplicitExposeAnnotation);
        this.networkUtil = Preconditions.checkNotNull(networkUtil, "networkUtil");
        this.elasticSearchHelper = new ElasticSearchHelper();
    }

    /**
     * Creates an instance of the {@link HttpDataManager}. The manager will set the value of
     * "useExplicitExposeAnnotation" to false.
     *
     * @param networkUtil The {@link NetworkUtil} to be used for checking the network state.
     */
    public HttpDataManager(NetworkUtil networkUtil) {
        this(networkUtil, false);
    }

    /**
     * Creates an instance of the {@link HttpDataManager}. The manager will set the value of
     * "useExplicitExposeAnnotation" to false, and will use {@link ElasticSearchNetworkUtil} as the
     * {@link NetworkUtil}.
     */
    public HttpDataManager() {
        this(new ElasticSearchNetworkUtil());
    }

    /**
     * Creates an instance of {@link HttpDataManager}. The manager will use {@link ElasticSearchNetworkUtil}
     * as the {@link NetworkUtil}.
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public HttpDataManager(boolean useExplicitExposeAnnotation) {
        this(new ElasticSearchNetworkUtil(), useExplicitExposeAnnotation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyExists(DataKey key) throws IOException, ServiceNotAvailableException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        return elasticSearchHelper.checkPathExists(key.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getData(DataKey key, Type typeOfT) throws IOException, ServiceNotAvailableException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        try {
            return deserialize(elasticSearchHelper.getJson(key.toString()), typeOfT);
        }
        catch (Resources.NotFoundException e) {
            throw new DataKeyNotFoundException(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException, ServiceNotAvailableException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        elasticSearchHelper.putJson(serialize(obj, typeOfT), key.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteIfExists(DataKey key) throws IOException, ServiceNotAvailableException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        elasticSearchHelper.sendDeleteRequestAtPath(key.toString());
    }

    /**
     * True, if the phone is online, else false.
     *
     * @return True, if the phone is online, else false.
     */
    @Override
    public boolean isOperational() {
        return networkUtil.isConnected(TradeApp.getContext());
    }

    /**
     * Returns true.
     */
    @Override
    public boolean requiresNetwork() {
        return true;
    }
}

