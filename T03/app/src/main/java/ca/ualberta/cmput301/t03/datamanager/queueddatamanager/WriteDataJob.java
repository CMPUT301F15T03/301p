package ca.ualberta.cmput301.t03.datamanager.queueddatamanager;


import java.io.IOException;
import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;

/**
 * Created by rishi on 15-11-11.
 */
public class WriteDataJob<T> extends DataManagerJob {

    private final T obj;
    private final Type type;

    public WriteDataJob(DataManager dataManager,
                        DataKey dataKey,
                        T obj,
                        Type typeOfT,
                        boolean requiresNetwork,
                        OnRequestFailedCallback onRequestFailedCallback,
                        OnRequestQueuedCallback onRequestQueuedCallback) {
        super(dataManager, dataKey, requiresNetwork, onRequestFailedCallback,
                onRequestQueuedCallback);
        this.obj = Preconditions.checkNotNull(obj, "obj");
        this.type = Preconditions.checkNotNull(typeOfT, "typeOfT");
    }

    public WriteDataJob(DataManager dataManager,
                        DataKey dataKey,
                        T obj,
                        Type typeOfT,
                        boolean requiresNetwork,
                        OnRequestFailedCallback onRequestFailedCallback) {
        this(dataManager, dataKey, obj, typeOfT, requiresNetwork, onRequestFailedCallback, null);
    }

    @Override
    public void onRun() throws IOException {
        dataManager.writeData(dataKey, obj, type);
    }
}
