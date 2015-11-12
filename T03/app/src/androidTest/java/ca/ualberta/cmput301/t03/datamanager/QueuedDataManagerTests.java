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
    @Override
    protected QueuedDataManager createNewDataManager() {
        return new QueuedDataManager(new HttpDataManager(getContext()), getContext());
    }

    @Override
    public void testKeyExists() {
        super.keyExistsTest();
    }

    @Override
    public void testGetDataWhenKeyDoesNotExistThrowsException() {
        super.getDataWhenKeyDoesNotExistThrowsExceptionTest();
    }

    @Override
    public void testWriteData() {
        super.writeDataTest();
    }

    @Override
    public void testDelete() {
        super.deleteTest();
    }

    @Override
    public void testIsOperational() {
        super.isOperationalTest();
    }

    public void testWriteThenGetDataWhenInnerDataManagerNotAvailable() throws IOException {

        InMemoryDataManager mockDataManager = new InMemoryDataManager();
        QueuedDataManager queuedDataManager = new QueuedDataManager(mockDataManager, getContext());

        Type type = new TypeToken<TestDto>() {
        }.getType();

        assertFalse(queuedDataManager.keyExists(dataKey));
        queuedDataManager.writeData(dataKey, testDto, type);
        mockDataManager.setIsOperational(false);
        assertFalse(queuedDataManager.isOperational());
        assertTrue(queuedDataManager.keyExists(dataKey));
        TestDto retrieved = queuedDataManager.getData(dataKey, type);
        assertEquals(testDto, retrieved);
        mockDataManager.setIsOperational(true);
        queuedDataManager.deleteIfExists(dataKey);
        assertFalse(queuedDataManager.keyExists(dataKey));
    }

    public void testWriteWhenInnerManagerIsNotAvailableThrowsException() throws IOException {
        InMemoryDataManager mockDataManager = new InMemoryDataManager();
        final QueuedDataManager queuedDataManager = new QueuedDataManager(mockDataManager, getContext());

        final Type type = new TypeToken<TestDto>() {
        }.getType();

        assertFalse(queuedDataManager.keyExists(dataKey));
        mockDataManager.setIsOperational(false);
        assertFalse(queuedDataManager.isOperational());
        assertThrowsException(new Runnable() {
            @Override
            public void run() {
                try {
                    queuedDataManager.writeData(dataKey, testDto, type); // throws
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, ServiceNotAvailableException.class);
        assertFalse(queuedDataManager.keyExists(dataKey));
        mockDataManager.setIsOperational(true);
        assertTrue(queuedDataManager.isOperational());
        assertFalse(queuedDataManager.keyExists(dataKey));
    }
}
