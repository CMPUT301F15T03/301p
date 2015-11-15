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

package ca.ualberta.cmput301.t03.datamanager.elasticsearch;

import android.content.Context;

import com.path.android.jobqueue.network.NetworkEventProvider;
import com.path.android.jobqueue.network.NetworkUtil;
import com.path.android.jobqueue.network.NetworkUtilImpl;

import ca.ualberta.cmput301.t03.TradeApp;

/**
 * A {@link NetworkUtil} and {@link NetworkEventProvider} implementation for checking if the
 * Elastic Search server can be contacted.
 * Created by rishi on 15-11-12.
 */
public class ElasticSearchNetworkUtil implements NetworkUtil, NetworkEventProvider {

    private final NetworkUtilImpl networkUtil;

    /**
     * Creates an instance of {@link ElasticSearchNetworkUtil}.
     */
    public ElasticSearchNetworkUtil() {
        this.networkUtil = new NetworkUtilImpl(TradeApp.getContext());
    }

    /**
     * Sets a listener for the network changed events.
     * @param listener A {@link com.path.android.jobqueue.network.NetworkEventProvider.Listener}.
     */
    @Override
    public void setListener(Listener listener) {
        networkUtil.setListener(listener);
    }

    /**
     * Tells whether there is internet access available or not.
     * @param context The context to be used for checking the network connection.
     * @return True, if internet connection is available, else false.
     */
    @Override
    public boolean isConnected(Context context) {
        return networkUtil.isConnected(context);
    }
}
