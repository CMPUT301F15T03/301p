package ca.ualberta.cmput301.t03.datamanager.queueddatamanager;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;

/**
 * Created by rishi on 15-11-11.
 */
public abstract class DataManagerJob extends Job {
    private final static int DEFAULT_PRIORITY = 1;
    private final static String GROUP_ID = "datamanagerjob";

    protected final DataManager dataManager;
    protected final DataKey dataKey;

    private final OnRequestQueuedCallback onRequestQueuedCallback;

    public DataManagerJob(DataManager dataManager, DataKey dataKey,
                          OnRequestQueuedCallback onRequestQueuedCallback) {

        super(dataManager.requiresNetwork() ?
                  new Params(DEFAULT_PRIORITY).persist().requireNetwork().groupBy(GROUP_ID)
                : new Params(DEFAULT_PRIORITY).persist().groupBy(GROUP_ID));
        this.dataManager = Preconditions.checkNotNull(dataManager, "dataManager");
        this.dataKey = Preconditions.checkNotNull(dataKey, "dataKey");
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

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    public interface OnRequestQueuedCallback {
        void onRequestQueued();
    }
}
