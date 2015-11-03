package ca.ualberta.cmput301.t03.datamanager;

import java.net.MalformedURLException;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.datamanager.httpdatamanager.HttpDataManager;

/**
 * Created by rishi on 15-10-30.
 */
public class HttpDataManagerTests extends BaseDataManagerTests<HttpDataManager> implements DataManagerApiTests {

    @Override
    protected HttpDataManager createNewDataManager() {
        return new HttpDataManager(getContext());
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
