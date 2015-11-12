package ca.ualberta.cmput301.t03.datamanager.elasticsearch;

import android.content.Context;

import com.path.android.jobqueue.network.NetworkEventProvider;
import com.path.android.jobqueue.network.NetworkUtil;
import com.path.android.jobqueue.network.NetworkUtilImpl;

import ca.ualberta.cmput301.t03.TradeApp;

/**
 * Elastic search specific logic goes here
 * Created by rishi on 15-11-12.
 */
public class ElasticSearchNetworkUtil implements NetworkUtil, NetworkEventProvider {

    private final NetworkUtilImpl networkUtil;

    public ElasticSearchNetworkUtil() {
        this.networkUtil = new NetworkUtilImpl(TradeApp.getInstance());
    }

    @Override
    public void setListener(Listener listener) {
        networkUtil.setListener(listener);
    }

    @Override
    public boolean isConnected(Context context) {
        return networkUtil.isConnected(context);
    }
}
