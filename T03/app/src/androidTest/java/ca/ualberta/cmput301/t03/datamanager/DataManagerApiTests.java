package ca.ualberta.cmput301.t03.datamanager;

/**
 * Created by rishi on 15-10-30.
 */
public interface DataManagerApiTests {
    void testKeyExists();
    void testGetDataWhenKeyDoesNotExistThrowsException();
    void testWriteData();
    void testDelete();
    void testIsOperational();
}
