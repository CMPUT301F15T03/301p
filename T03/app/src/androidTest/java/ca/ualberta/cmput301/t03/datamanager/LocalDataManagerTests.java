package ca.ualberta.cmput301.t03.datamanager;

/**
 * Created by rishi on 15-10-30.
 */
public class LocalDataManagerTests extends BaseDataManagerTests<LocalDataManager> implements DataManagerApiTests {

    @Override
    protected LocalDataManager createNewDataManager() {
        return new LocalDataManager(getContext());
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
}
