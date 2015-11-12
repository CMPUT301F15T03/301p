package ca.ualberta.cmput301.t03.datamanager.jobs;


import java.io.IOException;

import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.ElasticSearchHelper;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;

/**
 * Created by rishi on 15-11-11.
 */
public class DeleteDataJob extends DataManagerJob {

    public DeleteDataJob(DataKey dataKey) {
                         //OnRequestQueuedCallback onRequestQueuedCallback) {
        super(dataKey);//, onRequestQueuedCallback);
    }

//    public DeleteDataJob(DataKey dataKey) {
//        this(dataKey, null);
//    }

    @Override
    public void onRun() throws IOException {
        new ElasticSearchHelper().sendDeleteRequestAtPath(getRequestSuffix());
    }
}
