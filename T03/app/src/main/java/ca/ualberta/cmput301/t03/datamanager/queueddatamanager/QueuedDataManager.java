package ca.ualberta.cmput301.t03.datamanager.queueddatamanager;

import android.content.Context;

import com.path.android.jobqueue.JobManager;

import java.io.IOException;
import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.CachedDataManager;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.JsonDataManager;

/**
 * Created by rishi on 15-11-11.
 */
public class QueuedDataManager extends CachedDataManager {

    private final JobManager jobManager;

    public QueuedDataManager(JsonDataManager innerManager, Context context, boolean useExplicitExposeAnnotation) {
        super(innerManager, context, useExplicitExposeAnnotation);
        Preconditions.checkNotNull(context, "context");
        jobManager = new JobManager(context);
    }

    public QueuedDataManager(JsonDataManager innerManager, Context context) {
        super(innerManager, context);
        Preconditions.checkNotNull(context, "context");
        jobManager = new JobManager(context);
    }

    @Override
    public <T> void writeData(final DataKey key, final T obj, final Type typeOfT) throws IOException {
        WriteDataJob<T> job = new WriteDataJob<>(innerManager, key, obj, typeOfT,
                new DataManagerJob.OnRequestQueuedCallback() {
                    @Override
                    public void onRequestQueued() {
                        writeToCache(key, obj, typeOfT);
                    }
                });
        jobManager.addJobInBackground(job);
    }

    @Override
    public void deleteIfExists(final DataKey key) throws IOException {
        DeleteDataJob job = new DeleteDataJob(innerManager, key,
                new DataManagerJob.OnRequestQueuedCallback() {
                @Override
                public void onRequestQueued() {
                    deleteFromCache(key);
                }
            });
        jobManager.addJobInBackground(job);
    }

    @Override
    public boolean isOperational() {
        return true;
    }
}
