package ca.ualberta.cmput301.t03.datamanager;

import android.test.AndroidTestCase;

import com.google.gson.reflect.TypeToken;

import static ca.ualberta.cmput301.t03.commontesting.ExceptionAsserter.assertThrowsException;

/**
 * Created by rishi on 15-10-30.
 */
// Source: http://developer.android.com/reference/android/test/AndroidTestCase.html
public class LocalDataManagerTests extends AndroidTestCase {

    private LocalDataManager localDataManager;
    private TestDto testDto;
    private DataKey dataKey;

    public void setUp() {
        localDataManager = new LocalDataManager(this.getContext());
        testDto = new TestDto(100, "Hundred", false, "a hidden string");
        dataKey = new DataKey("TestDto", "123");
    }

    public void testKeyExists() {
        assertFalse(localDataManager.keyExists(dataKey));
        localDataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {}.getType());
        assertTrue(localDataManager.keyExists(dataKey));
        assertFalse(localDataManager.keyExists(new DataKey("not", "exists")));
    }

    public void testGetDataWhenKeyDoesNotExistThrowsException() {
        assertFalse(localDataManager.keyExists(dataKey));
        assertThrowsException(new Runnable() {
            @Override
            public void run() {
                localDataManager.getData(dataKey, new TypeToken<TestDto>() {}.getType());
            }
        }, DataKeyNotFoundException.class);
    }

    public void testWriteData() {
        localDataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {}.getType());
        assertTrue(localDataManager.keyExists(dataKey));
        TestDto receivedData = localDataManager.getData(dataKey, new TypeToken<TestDto>() {}.getType());
        assertEquals(testDto, receivedData);
    }

    public void testDelete() {
        localDataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {}.getType());
        assertTrue(localDataManager.keyExists(dataKey));
        localDataManager.delete(dataKey);
        assertFalse(localDataManager.keyExists(dataKey));
    }

    public void testIsOperational() {
        assertTrue(localDataManager.isOperational());
    }
}
