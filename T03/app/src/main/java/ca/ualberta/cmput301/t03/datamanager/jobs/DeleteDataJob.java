package ca.ualberta.cmput301.t03.datamanager.jobs;


import java.io.IOException;

import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.ElasticSearchHelper;

/**
 * Created by rishi on 15-11-11.
 */
public class DeleteDataJob extends DataManagerJob {

    public DeleteDataJob(DataKey dataKey) {
        super(dataKey);
    }

    @Override
    public void onRun() throws IOException {
        new ElasticSearchHelper().sendDeleteRequestAtPath(getRequestSuffix());
    }
}
