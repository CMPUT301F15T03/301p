package ca.ualberta.cmput301.t03;

import android.app.Application;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

/**
 * Created by rishi on 15-11-12.
 */
// Source: https://github.com/yigit/android-priority-jobqueue/blob/master/examples/twitter/TwitterClient/src/com/path/android/jobqueue/examples/twitter/TwitterApplication.java
public class TradeApp extends Application {

    private static TradeApp instance;
    private JobManager jobManager;

    public TradeApp() {
        instance = this;
    }

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

    public static TradeApp getInstance() {
        return instance;
    }

    public JobManager getJobManager() {
        return jobManager;
    }
}
