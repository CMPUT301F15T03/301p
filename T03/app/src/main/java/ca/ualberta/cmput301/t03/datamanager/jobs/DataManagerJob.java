package ca.ualberta.cmput301.t03.datamanager.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.datamanager.DataKey;

/**
 * Created by rishi on 15-11-11.
 */
public abstract class DataManagerJob extends Job {
    private static final int PRIORITY = 1000;
    private static final String GROUP = "datamanagerjob";

    private final String type;
    private final String id;

    public DataManagerJob(DataKey dataKey) {

        super(new Params(PRIORITY).persist().requireNetwork().groupBy(GROUP));
        Preconditions.checkNotNull(dataKey, "dataKey");
        this.type = dataKey.getType();
        this.id = dataKey.getId();
    }

    @Override
    public void onAdded() { }

    @Override
    protected void onCancel() {
        throw new NotImplementedException();
    }

    // Needed because only simple types can be serialized
    protected String getRequestSuffix() {
        return new DataKey(type, id).toString();
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }
}
