package ca.ualberta.cmput301.t03.datamanager;

import android.test.AndroidTestCase;

import com.google.gson.reflect.TypeToken;

import static ca.ualberta.cmput301.t03.commontesting.ExceptionAsserter.assertThrowsException;

/**
 * Created by rishi on 15-10-30.
 */
// Source: http://developer.android.com/reference/android/test/AndroidTestCase.html
public abstract class BaseDataManagerTests<T extends DataManager> extends AndroidTestCase {
    protected T dataManager;
    protected TestDto testDto;
    protected DataKey dataKey;

    protected abstract T createNewDataManager();

    public void setUp() {
        dataManager = createNewDataManager();
        testDto = new TestDto(100, "Hundred", false, "a hidden string");
        dataKey = new DataKey("TestDto", "123");
    }

    protected void keyExistsTest() {
        assertFalse(dataManager.keyExists(dataKey));
        dataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {
        }.getType());
        assertTrue(dataManager.keyExists(dataKey));
        assertFalse(dataManager.keyExists(new DataKey("not", "exists")));
        assertTrue(dataManager.deleteIfExists(dataKey));
    }

    protected void getDataWhenKeyDoesNotExistThrowsExceptionTest() {
        assertFalse(dataManager.keyExists(dataKey));
        assertThrowsException(new Runnable() {
            @Override
            public void run() {
                dataManager.getData(dataKey, new TypeToken<TestDto>() {
                }.getType());
            }
        }, DataKeyNotFoundException.class);
    }

    protected void writeDataTest() {
        dataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {}.getType());
        assertTrue(dataManager.keyExists(dataKey));
        TestDto receivedData = dataManager.getData(dataKey, new TypeToken<TestDto>() {}.getType());
        assertEquals(testDto, receivedData);
        assertTrue(dataManager.deleteIfExists(dataKey));
    }

    protected void deleteTest() {
        assertFalse(dataManager.keyExists(dataKey));
        assertFalse(dataManager.deleteIfExists(dataKey));
        dataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {
        }.getType());
        assertTrue(dataManager.keyExists(dataKey));
        assertTrue(dataManager.deleteIfExists(dataKey));
        assertFalse(dataManager.keyExists(dataKey));
    }

    protected void isOperationalTest() {
        assertTrue(dataManager.isOperational());
    }
}
