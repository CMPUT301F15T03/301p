package ca.ualberta.cmput301.t03.datamanager.jobs;


import java.io.IOException;

import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;

/**
 * Created by rishi on 15-11-11.
 */
public class DeleteDataJob extends DataManagerJob {

    public DeleteDataJob(DataManager dataManager,
                         DataKey dataKey,
                         OnRequestQueuedCallback onRequestQueuedCallback) {
        super(dataManager, dataKey, onRequestQueuedCallback);
    }

    public DeleteDataJob(DataManager dataManager,
                         DataKey dataKey) {
        this(dataManager, dataKey, null);
    }

    @Override
    public void onRun() throws IOException {
        dataManager.deleteIfExists(dataKey);
    }
}
