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
public abstract class DataManagerJob<T> extends Job {
    private final static int DEFAULT_PRIORITY = 1;
    private final static String GROUP_ID = "datamanagerjob";

    protected final DataManager dataManager;
    protected final DataKey dataKey;
    protected final T obj;
    protected final Type type;
    private final OnRequestQueuedCallback onRequestQueuedCallback;

    public DataManagerJob(DataManager dataManager, DataKey dataKey, T obj, Type typeOfT,
                          boolean requiresNetwork, OnRequestQueuedCallback onRequestQueuedCallback) {

        super(requiresNetwork ? new Params(DEFAULT_PRIORITY).persist().requireNetwork().groupBy(GROUP_ID)
                : new Params(DEFAULT_PRIORITY).persist().groupBy(GROUP_ID));
        this.dataManager = Preconditions.checkNotNull(dataManager, "dataManager");
        this.dataKey = Preconditions.checkNotNull(dataKey, "dataKey");
        this.obj = Preconditions.checkNotNull(obj, "obj");
        this.type = Preconditions.checkNotNull(typeOfT, "typeOfT");
        this.onRequestQueuedCallback = onRequestQueuedCallback;
    }

    public DataManagerJob(DataManager dataManager, DataKey dataKey, T obj, Type typeOfT,
                          boolean requiresNetwork) {
        this(dataManager, dataKey, obj, typeOfT, requiresNetwork, null);
    }

    @Override
    public void onAdded() {
        if (onRequestQueuedCallback != null) {
            onRequestQueuedCallback.onRequestQueued();
        }
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    public interface OnRequestQueuedCallback {
        void onRequestQueued();
    }
}
