package ca.ualberta.cmput301.t03.datamanager;


import com.path.android.jobqueue.JobManager;

import java.io.IOException;
import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.jobs.DeleteDataJob;
import ca.ualberta.cmput301.t03.datamanager.jobs.WriteDataJob;

/**
 * Created by rishi on 15-11-11.
 */
public class QueuedDataManager extends CachedDataManager {

    private final JobManager jobManager;

    public QueuedDataManager(boolean useExplicitExposeAnnotation) {
        this(new HttpDataManager(useExplicitExposeAnnotation), useExplicitExposeAnnotation);
    }

    public QueuedDataManager() {
        this(false);
    }

    protected QueuedDataManager(HttpDataManager innerManager, boolean useExplicitExposeAnnotation) {
        super(Preconditions.checkNotNull(innerManager, "innerManager"), useExplicitExposeAnnotation);
        jobManager = TradeApp.getInstance().getJobManager();
    }

    @Override
    public <T> void writeData(final DataKey key, final T obj, final Type typeOfT) throws IOException {
        String json = serialize(obj, typeOfT);
        WriteDataJob job = new WriteDataJob(key, json,
                innerManager.jsonFormatter.getUseExplicitExposeAnnotation());
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
