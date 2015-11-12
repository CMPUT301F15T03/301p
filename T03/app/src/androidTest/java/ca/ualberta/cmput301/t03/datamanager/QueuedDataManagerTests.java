package ca.ualberta.cmput301.t03.datamanager;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.network.NetworkEventProvider;
import com.path.android.jobqueue.network.NetworkUtil;

import java.io.IOException;
import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.datamanager.mocks.MockNetworkUtil;
import ca.ualberta.cmput301.t03.datamanager.mocks.TestDto;

/**
 * Created by rishi on 15-11-11.
 */
public class QueuedDataManagerTests extends BaseDataManagerTests<QueuedDataManager> implements DataManagerApiTests {

    private static final int DELAY_MS = 500;

    @Override
    protected QueuedDataManager createNewDataManager() {
        return new QueuedDataManager(TradeApp.getInstance().getJobManager());
    }

    @Override
    public void testKeyExists() {
        super.keyExistsTest(DELAY_MS);
    }

    @Override
    public void testGetDataWhenKeyDoesNotExistThrowsException() {
        super.getDataWhenKeyDoesNotExistThrowsExceptionTest();
    }

    @Override
    public void testWriteData() {
        super.writeDataTest(DELAY_MS);
    }

    @Override
    public void testDelete() {
        super.deleteTest(DELAY_MS);
    }

    @Override
    public void testIsOperational() {
        super.isOperationalTest();
    }


    public void testWriteThenGetDataWhenInnerDataManagerNotAvailable() throws IOException, InterruptedException {

        // QDM doesn't change the getData and keyExists implementations of CachedDataManager
        // Test to check that the write (when device is online) is not messing with these methods

        MockNetworkUtil mockNetworkUtil = new MockNetworkUtil();

        QueuedDataManager queuedDataManager = new QueuedDataManager(new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtil)
                        .build()),
                new HttpDataManager(mockNetworkUtil));

        Type type = new TypeToken<TestDto>() {
        }.getType();

        assertFalse(queuedDataManager.keyExists(dataKey));
        queuedDataManager.writeData(dataKey, testDto, type);
        Thread.sleep(DELAY_MS);
        assertTrue(queuedDataManager.innerManager.keyExists(dataKey));
        assertTrue(queuedDataManager.keyExists(dataKey));
        mockNetworkUtil.setNetworkState(false);
        assertTrue(queuedDataManager.isOperational());
        assertFalse(queuedDataManager.innerManager.isOperational());
        assertTrue(queuedDataManager.keyExists(dataKey));
        TestDto retrieved = queuedDataManager.getData(dataKey, type);
        assertEquals(testDto, retrieved);
        mockNetworkUtil.setNetworkState(true);
        queuedDataManager.deleteIfExists(dataKey);
        Thread.sleep(DELAY_MS);
        assertFalse(queuedDataManager.keyExists(dataKey));
        assertFalse(queuedDataManager.innerManager.keyExists(dataKey));
    }
}
