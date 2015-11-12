package ca.ualberta.cmput301.t03.datamanager.queueddatamanager;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.common.Preconditions;
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
    private final OnRequestFailedCallback onRequestFailedCallback;

    public DataManagerJob(DataManager dataManager, DataKey dataKey, boolean requiresNetwork,
                          OnRequestFailedCallback onRequestFailedCallback,
                          OnRequestQueuedCallback onRequestQueuedCallback) {

        super(requiresNetwork ? new Params(DEFAULT_PRIORITY).persist().requireNetwork().groupBy(GROUP_ID)
                : new Params(DEFAULT_PRIORITY).persist().groupBy(GROUP_ID));
        this.dataManager = Preconditions.checkNotNull(dataManager, "dataManager");
        this.dataKey = Preconditions.checkNotNull(dataKey, "dataKey");
        this.onRequestFailedCallback = Preconditions.checkNotNull(onRequestFailedCallback, "onRequestFailedCallback");
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
        if (onRequestFailedCallback != null) {
            onRequestFailedCallback.onRequestFailed();
        }
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    public interface OnRequestQueuedCallback {
        void onRequestQueued();
    }

    public interface OnRequestFailedCallback {
        void onRequestFailed();
    }
}
