package ca.ualberta.cmput301.t03;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

import java.io.IOException;

import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.QueuedDataManager;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.CachedQueryExecutor;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.HttpQueryExecutor;
import ca.ualberta.cmput301.t03.datamanager.elasticsearch.queries.QueryExecutor;
import ca.ualberta.cmput301.t03.trading.Trade;
import ca.ualberta.cmput301.t03.trading.TradeList;
import ca.ualberta.cmput301.t03.user.User;

/**
 * Main {@link Application} class that loads when the app is loaded. Exposes static ways to
 * access app wide objects.
 * Created by rishi on 15-11-12.
 */
// Source: https://github.com/yigit/android-priority-jobqueue/blob/master/examples/twitter/TwitterClient/src/com/path/android/jobqueue/examples/twitter/TwitterApplication.java
public class TradeApp extends Application {

    private static TradeApp instance;
    private JobManager jobManager;
    private Boolean notificationThreadStarted;

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

    public static void startNotificationService() {
        instance.startNotificationThread();
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

    private void startNotificationThread() {
        if (notificationThreadStarted == null || !notificationThreadStarted) {
            notificationThreadStarted = true;
            Thread notificationThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String username = PrimaryUser.getInstance().getUsername();

                    while (true) {
                        User user = new User(username, TradeApp.getContext());
                        TradeList list = null;
                        try {
                            list = user.getTradeList();
                        } catch (IOException | ServiceNotAvailableException e) {
                            list = null;
                            // do nothing
                        }
                        if (list != null)
                            for (Trade trade : list.getTradesAsList()) {
                                if (!trade.getHasBeenNotified() && !trade.getHasBeenSeen()) {
                                    // do a notify
                                    NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                            .setSmallIcon(R.drawable.ic_add)
                                            .setContentTitle("You've Received a New Trade!!")
                                            .setContentText(trade.getBorrower().getUsername() + " Wants " + trade.getOwnersItems().get(0).getItemName())
                                            .setAutoCancel(true);
                                    // Creates an explicit intent for an Activity in your app
                                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    resultIntent.putExtra("INTENT", "TRADES");

                                    // The stack builder object will contain an artificial back stack for the
                                    // started Activity.
                                    // This ensures that navigating backward from the Activity leads out of
                                    // your application to the Home screen.
                                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                                    // Adds the back stack for the Intent (but not the Intent itself)
                                    stackBuilder.addParentStack(MainActivity.class);
                                    // Adds the Intent that starts the Activity to the top of the stack
                                    stackBuilder.addNextIntent(resultIntent);
                                    PendingIntent resultPendingIntent =
                                            stackBuilder.getPendingIntent(
                                                    0,
                                                    PendingIntent.FLAG_UPDATE_CURRENT
                                            );
                                    mBuilder.setContentIntent(resultPendingIntent);
                                    NotificationManager mNotificationManager =
                                                (NotificationManager) TradeApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                    // mId allows you to update the notification later on.
                                    mNotificationManager.notify(0, mBuilder.build());

                                    // set the trade as has been notified
                                    trade.setHasBeenNotified(true);
                                    try {
                                        trade.commitChanges();
                                    } catch (ServiceNotAvailableException e) {
                                        // don't need to do anything
                                    }
                                }
                            }
                        SystemClock.sleep(10000);
                    }
                }
            });
            notificationThread.start();
        }
    }
}
