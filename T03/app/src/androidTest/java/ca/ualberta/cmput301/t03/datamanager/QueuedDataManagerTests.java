package ca.ualberta.cmput301.t03.datamanager;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by rishi on 15-11-11.
 */
public class QueuedDataManagerTests extends BaseDataManagerTests<QueuedDataManager> implements DataManagerApiTests {

    private static final int DELAY_MS = 500;

    @Override
    protected QueuedDataManager createNewDataManager() {
        return new QueuedDataManager();
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

        TestHttpDataManager mockDataManager = new TestHttpDataManager();
        QueuedDataManager queuedDataManager = new QueuedDataManager(mockDataManager, false);

        Type type = new TypeToken<TestDto>() {
        }.getType();

        assertFalse(queuedDataManager.keyExists(dataKey));
        queuedDataManager.writeData(dataKey, testDto, type);
        Thread.sleep(DELAY_MS);
        assertTrue(mockDataManager.keyExists(dataKey));
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

    private class TestHttpDataManager extends HttpDataManager {
        private boolean isOperational = true;

        public void setIsOperational(boolean isOperational) {
            this.isOperational = isOperational;
        }

        @Override
        public boolean isOperational() {
            return isOperational;
        }
    }
}
