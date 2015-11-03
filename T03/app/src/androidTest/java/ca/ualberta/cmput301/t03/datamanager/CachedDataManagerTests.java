package ca.ualberta.cmput301.t03.datamanager;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.HashMap;

import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.datamanager.httpdatamanager.HttpDataManager;
import static ca.ualberta.cmput301.t03.commontesting.ExceptionAsserter.assertThrowsException;

/**
 * Created by rishi on 15-10-31.
 */
public class CachedDataManagerTests extends BaseDataManagerTests<CachedDataManager> implements DataManagerApiTests {
    @Override
    protected CachedDataManager createNewDataManager() {
        return new CachedDataManager(new HttpDataManager(getContext()), getContext());
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
        CachedDataManager cachedDataManager = new CachedDataManager(mockDataManager, getContext());

        Type type = new TypeToken<TestDto>() {}.getType();

        assertFalse(cachedDataManager.keyExists(dataKey));
        cachedDataManager.writeData(dataKey, testDto, type);
        mockDataManager.setIsOperational(false);
        assertFalse(cachedDataManager.isOperational());
        assertTrue(cachedDataManager.keyExists(dataKey));
        TestDto retrieved = cachedDataManager.getData(dataKey, type);
        assertEquals(testDto, retrieved);
        mockDataManager.setIsOperational(true);
        assertTrue(cachedDataManager.deleteIfExists(dataKey));
        assertFalse(cachedDataManager.keyExists(dataKey));
    }

    public void testWriteWhenInnerManagerIsNotAvailableThrowsException() throws IOException {
        InMemoryDataManager mockDataManager = new InMemoryDataManager();
        final CachedDataManager cachedDataManager = new CachedDataManager(mockDataManager, getContext());

        final Type type = new TypeToken<TestDto>() {}.getType();

        assertFalse(cachedDataManager.keyExists(dataKey));
        mockDataManager.setIsOperational(false);
        assertFalse(cachedDataManager.isOperational());
        assertThrowsException(new Runnable() {
            @Override
            public void run() {
                try {
                    cachedDataManager.writeData(dataKey, testDto, type); // throws
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, ServiceNotAvailableException.class);
        assertFalse(cachedDataManager.keyExists(dataKey));
        mockDataManager.setIsOperational(true);
        assertTrue(cachedDataManager.isOperational());
        assertFalse(cachedDataManager.keyExists(dataKey));
    }

    private class InMemoryDataManager extends JsonDataManager {
        private boolean isOperational = true;
        private HashMap<String, Object> inMemoryDataRepository = new HashMap<>();

        @Override
        public boolean keyExists(DataKey key) throws IOException {
            return inMemoryDataRepository.containsKey(key.toString());
        }

        @Override
        public <T> T getData(DataKey key, Type typeOfT) throws IOException {
            return (T)inMemoryDataRepository.get(key.toString());
        }

        @Override
        public <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException {
            inMemoryDataRepository.put(key.toString(), obj);
        }

        @Override
        public boolean deleteIfExists(DataKey key) throws IOException {
            return inMemoryDataRepository.remove(key.toString()) != null;
        }

        @Override
        public boolean isOperational() {
            return isOperational;
        }

        public void setIsOperational(boolean isOperational) {
            this.isOperational = isOperational;
        }
    }
}
