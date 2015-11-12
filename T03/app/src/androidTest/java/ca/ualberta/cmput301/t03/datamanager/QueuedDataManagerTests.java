package ca.ualberta.cmput301.t03.datamanager;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.datamanager.queueddatamanager.QueuedDataManager;

import static ca.ualberta.cmput301.t03.common.ExceptionAsserter.assertThrowsException;

/**
 * Created by rishi on 15-11-11.
 */
public class QueuedDataManagerTests extends BaseDataManagerTests<QueuedDataManager> implements DataManagerApiTests {

    private static final int DELAY_MS = 500;

    @Override
    protected QueuedDataManager createNewDataManager() {
        return new QueuedDataManager(new HttpDataManager(getContext()), getContext());
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

        InMemoryDataManager mockDataManager = new InMemoryDataManager();
        QueuedDataManager queuedDataManager = new QueuedDataManager(mockDataManager, getContext());

        Type type = new TypeToken<TestDto>() {
        }.getType();

        assertFalse(queuedDataManager.keyExists(dataKey));
        queuedDataManager.writeData(dataKey, testDto, type);
        Thread.sleep(DELAY_MS);
        mockDataManager.setIsOperational(false);
        assertTrue(queuedDataManager.isOperational());
        assertTrue(queuedDataManager.keyExists(dataKey));
        TestDto retrieved = queuedDataManager.getData(dataKey, type);
        assertEquals(testDto, retrieved);
        mockDataManager.setIsOperational(true);
        queuedDataManager.deleteIfExists(dataKey);
        Thread.sleep(DELAY_MS);
        assertFalse(queuedDataManager.keyExists(dataKey));
    }
}
