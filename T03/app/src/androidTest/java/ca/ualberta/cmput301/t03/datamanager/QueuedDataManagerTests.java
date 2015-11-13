package ca.ualberta.cmput301.t03.datamanager;

import com.google.gson.reflect.TypeToken;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
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

        Type type = new TypeToken<TestDto>() {}.getType();

        MockNetworkUtil mockNetworkUtil = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtil)
                        .build()),
                new HttpDataManager(mockNetworkUtil));

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        testDataManagerWithMockNetworkUtil.writeData(dataKey, testDto, type);
        Thread.sleep(DELAY_MS);
        assertTrue(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        mockNetworkUtil.setNetworkState(false);
        assertTrue(testDataManagerWithMockNetworkUtil.isOperational());
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.isOperational());
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        TestDto retrieved = testDataManagerWithMockNetworkUtil.getData(dataKey, type);
        assertEquals(testDto, retrieved);
        mockNetworkUtil.setNetworkState(true);
        testDataManagerWithMockNetworkUtil.deleteIfExists(dataKey);
        Thread.sleep(DELAY_MS);
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
    }

    public void testWriteWhenDeviceOfflineQueuesUpRequests() throws IOException, InterruptedException {
        Type type = new TypeToken<TestDto>() {}.getType();
        DataKey key2 = new DataKey(dataKey.getType(), dataKey.getId() + "2");
        MockNetworkUtil mockNetworkUtilForQueue = new MockNetworkUtil();
        MockNetworkUtil mockNetworkUtilForHttp = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtilForQueue)
                        .build()),
                new HttpDataManager(mockNetworkUtilForHttp));

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(key2));

        mockNetworkUtilForQueue.setNetworkState(false);
        mockNetworkUtilForHttp.setNetworkState(false);

        testDataManagerWithMockNetworkUtil.writeData(dataKey, testDto, type);
        Thread.sleep(DELAY_MS);

        testDataManagerWithMockNetworkUtil.writeData(key2, testDto, type);
        Thread.sleep(DELAY_MS);

        mockNetworkUtilForHttp.setNetworkState(true);
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(key2));
        mockNetworkUtilForHttp.setNetworkState(false);
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(key2));

        // Open Http first so that the worker threads don't start until HttpDataManager is ready
        mockNetworkUtilForHttp.setNetworkState(true);
        mockNetworkUtilForQueue.setNetworkState(true);
        Thread.sleep(DELAY_MS);

        assertTrue(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.innerManager.keyExists(key2));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(key2));

        testDataManagerWithMockNetworkUtil.deleteIfExists(dataKey);
        testDataManagerWithMockNetworkUtil.deleteIfExists(key2);
        Thread.sleep(DELAY_MS);
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(key2));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(key2));
    }

    public void testDeleteWhenDeviceOfflineQueuesUpRequests() throws IOException, InterruptedException {
        Type type = new TypeToken<TestDto>() {}.getType();
        DataKey key2 = new DataKey(dataKey.getType(), dataKey.getId() + "2");
        MockNetworkUtil mockNetworkUtilForQueue = new MockNetworkUtil();
        MockNetworkUtil mockNetworkUtilForHttp = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtilForQueue)
                        .build()),
                new HttpDataManager(mockNetworkUtilForHttp));

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(key2));

        testDataManagerWithMockNetworkUtil.writeData(dataKey, testDto, type);
        testDataManagerWithMockNetworkUtil.writeData(key2, testDto, type);
        Thread.sleep(DELAY_MS);

        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(key2));

        mockNetworkUtilForQueue.setNetworkState(false);
        mockNetworkUtilForHttp.setNetworkState(false);
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(key2));


        testDataManagerWithMockNetworkUtil.deleteIfExists(dataKey);
        testDataManagerWithMockNetworkUtil.deleteIfExists(key2);
        Thread.sleep(DELAY_MS);

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(key2));
        mockNetworkUtilForHttp.setNetworkState(true);
        assertTrue(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertTrue(testDataManagerWithMockNetworkUtil.innerManager.keyExists(key2));

        mockNetworkUtilForQueue.setNetworkState(true);
        Thread.sleep(DELAY_MS);
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(key2));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(dataKey));
        assertFalse(testDataManagerWithMockNetworkUtil.innerManager.keyExists(key2));
    }

    public void testQueueAndRequestDispatchOrdersAreSame() throws IOException, InterruptedException {
        Type type = new TypeToken<TestDto>() {}.getType();
        MockNetworkUtil mockNetworkUtilForQueue = new MockNetworkUtil();
        MockNetworkUtil mockNetworkUtilForHttp = new MockNetworkUtil();

        QueuedDataManager testDataManagerWithMockNetworkUtil = new QueuedDataManager(new JobManager(getContext(),
                new Configuration.Builder(getContext())
                        .minConsumerCount(1)
                        .maxConsumerCount(3)
                        .loadFactor(3)
                        .consumerKeepAlive(120)
                        .networkUtil(mockNetworkUtilForQueue)
                        .build()),
                new HttpDataManager(mockNetworkUtilForHttp));

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));

        mockNetworkUtilForQueue.setNetworkState(false);
        mockNetworkUtilForHttp.setNetworkState(false);

        testDataManagerWithMockNetworkUtil.writeData(dataKey, testDto, type);
        testDataManagerWithMockNetworkUtil.deleteIfExists(dataKey);

        mockNetworkUtilForHttp.setNetworkState(true);
        mockNetworkUtilForQueue.setNetworkState(true);
        Thread.sleep(DELAY_MS);

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));

        mockNetworkUtilForQueue.setNetworkState(false);
        mockNetworkUtilForHttp.setNetworkState(false);

        // Queue up 10 requests with different DTOs
        for (int i = 0; i < 10; ++i) {
            testDto.setaNumber(testDto.getaNumber() + i);
            testDataManagerWithMockNetworkUtil.writeData(dataKey, testDto, type);
        }

        mockNetworkUtilForHttp.setNetworkState(true);
        mockNetworkUtilForQueue.setNetworkState(true);
        Thread.sleep(DELAY_MS * 10);

        assertTrue(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
        TestDto retrieved = testDataManagerWithMockNetworkUtil.getData(dataKey, type);
        // Retrieved == last state of DTO
        assertEquals(testDto, retrieved);

        testDataManagerWithMockNetworkUtil.deleteIfExists(dataKey);
        Thread.sleep(DELAY_MS);

        assertFalse(testDataManagerWithMockNetworkUtil.keyExists(dataKey));
    }


    public void testWriteWhenDeviceOfflinePersistsRequests() {
        throw new NotImplementedException("To be implemented when we can kill the app and restart it.");
    }

    public void testDeleteWhenDeviceOfflinePersistsRequests() {
        throw new NotImplementedException("To be implemented when we can kill the app and restart it.");
    }

}
