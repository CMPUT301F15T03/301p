package ca.ualberta.cmput301.t03.datamanager.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;

/**
 * Created by rishi on 15-11-11.
 */
public abstract class DataManagerJob extends Job {
    private final String type;
    private final String id;

    private final OnRequestQueuedCallback onRequestQueuedCallback;

    public DataManagerJob(DataKey dataKey, OnRequestQueuedCallback onRequestQueuedCallback) {

        super(new Params(1000)/*.persist()*/.requireNetwork().groupBy("datamanagerjob"));
        Preconditions.checkNotNull(dataKey, "dataKey");
        this.type = dataKey.getType();
        this.id = dataKey.getId();
        this.onRequestQueuedCallback = onRequestQueuedCallback;
    }

    @Override
    public void onAdded() {
        if (onRequestQueuedCallback != null) {
            onRequestQueuedCallback.onRequestQueued();
        }
    }

    @Override
    protected void onCancel() {
        throw new NotImplementedException();
    }

    // Needed because only simple types can be serialized
    protected DataKey getDataKey() {
        return new DataKey(type, id);
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    public interface OnRequestQueuedCallback {
        void onRequestQueued();
    }
}
