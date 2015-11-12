package ca.ualberta.cmput301.t03.datamanager.queueddatamanager;


import java.io.IOException;

import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;

/**
 * Created by rishi on 15-11-11.
 */
public class DeleteDataJob extends DataManagerJob {

    public DeleteDataJob(DataManager dataManager,
                         DataKey dataKey,
                         OnRequestFailedCallback onRequestFailedCallback,
                         OnRequestQueuedCallback onRequestQueuedCallback) {
        super(dataManager, dataKey, onRequestFailedCallback, onRequestQueuedCallback);
    }

    public DeleteDataJob(DataManager dataManager,
                         DataKey dataKey,
                         OnRequestFailedCallback onRequestFailedCallback) {
        this(dataManager, dataKey, onRequestFailedCallback, null);
    }

    @Override
    public void onRun() throws IOException {
        dataManager.deleteIfExists(dataKey);
    }
}
