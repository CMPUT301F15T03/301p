package ca.ualberta.cmput301.t03.datamanager;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by rishi on 15-10-29.
 */
public interface DataManager {
    boolean keyExists(DataKey key) throws IOException;
    <T> T getData(DataKey key, Type typeOfT) throws IOException;
    <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException;
    boolean deleteIfExists(DataKey key) throws IOException;
    boolean isOperational();
}
