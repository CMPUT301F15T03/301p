package ca.ualberta.cmput301.t03;

import android.app.Application;
import android.content.Context;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.datamanager.QueuedDataManager;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.CachedQueryExecutor;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.HttpQueryExecutor;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.QueryExecutor;

/**
 * Main {@link Application} class that loads when the app is loaded. Exposes static ways to
 * access app wide objects.
 * Created by rishi on 15-11-12.
 */
// Source: https://github.com/yigit/android-priority-jobqueue/blob/master/examples/twitter/TwitterClient/src/com/path/android/jobqueue/examples/twitter/TwitterApplication.java
public class TradeApp extends Application {

    private static TradeApp instance;
    private JobManager jobManager;

    /**
     * Default constructor.
     */
    public TradeApp() {
        instance = this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Configuration configuration = new Configuration.Builder(this)
                .minConsumerCount(1)
                .maxConsumerCount(3)
                .loadFactor(3)
                .consumerKeepAlive(120)
                .build();

        jobManager = new JobManager(this, configuration);
    }

    /**
     * Gets the singleton instance of {@link TradeApp}.
     * @return The singleton instance of {@link TradeApp}.
     */
    public static TradeApp getInstance() {
        return instance;
    }

    /**
     * Gets the app level {@link Context}.
     * @return The app level {@link Context}.
     */
    public static Context getContext() {
        return getInstance();
    }

    /**
     * Gets the {@link JobManager} with lifetime same as the app.
     * @return The {@link JobManager}.
     */
    public JobManager getJobManager() {
        return jobManager;
    }

    /**
     * Creates a standard {@link DataManager} to be used by all the components of the app.
     * @param useExplicitExposeAnnotation If the @Expose annotation is to be used explicitly for
     *                                    choosing which fields to be used while serializing
     *                                    and de-serializing JSONs.
     * @return The {@link DataManager}.
     */
    public DataManager createDataManager(boolean useExplicitExposeAnnotation) {
        return new QueuedDataManager(getJobManager(), useExplicitExposeAnnotation);
    }

    public QueryExecutor createQueryExecutor() {
        return new CachedQueryExecutor(new HttpQueryExecutor());
    }
}
