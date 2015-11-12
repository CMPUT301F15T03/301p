package ca.ualberta.cmput301.t03.datamanager.jobs;

import java.io.IOException;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.ElasticSearchHelper;

/**
 * Created by rishi on 15-11-11.
 */
public class WriteDataJob extends DataManagerJob {

    private final String json;

    public WriteDataJob(DataKey dataKey, String json) {
        super(dataKey);
        this.json = Preconditions.checkNotNullOrWhitespace(json, "json");
    }

    @Override
    public void onRun() throws IOException {
        new ElasticSearchHelper().writeJson(json, getRequestSuffix());
    }
}
