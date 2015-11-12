package ca.ualberta.cmput301.t03.datamanager;


import com.path.android.jobqueue.JobManager;

import java.io.IOException;
import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.jobs.DeleteDataJob;
import ca.ualberta.cmput301.t03.datamanager.jobs.WriteDataJob;

/**
 * Created by rishi on 15-11-11.
 */
public class QueuedDataManager extends CachedDataManager {

    private final JobManager jobManager;

    public QueuedDataManager(JobManager jobManager, boolean useExplicitExposeAnnotation) {
        this(jobManager, new HttpDataManager(useExplicitExposeAnnotation));
    }

    public QueuedDataManager(JobManager jobManager) {
        this(jobManager, false);
    }

    protected QueuedDataManager(JobManager jobManager, HttpDataManager innerManager) {
        super(Preconditions.checkNotNull(innerManager, "innerManager"));
        this.jobManager = Preconditions.checkNotNull(jobManager, "jobManager");
    }

    @Override
    public <T> void writeData(final DataKey key, final T obj, final Type typeOfT) throws IOException {
        String json = serialize(obj, typeOfT);
        WriteDataJob job = new WriteDataJob(key, json);
        jobManager.addJobInBackground(job);
        writeToCache(key, obj, typeOfT);
    }

    @Override
    public void deleteIfExists(final DataKey key) throws IOException {
        DeleteDataJob job = new DeleteDataJob(key);
        jobManager.addJobInBackground(job);
        deleteFromCache(key);
    }

    @Override
    public boolean isOperational() {
        return true;
    }
}
