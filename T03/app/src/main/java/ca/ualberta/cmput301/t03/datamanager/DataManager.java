package ca.ualberta.cmput301.t03.datamanager;

import java.lang.reflect.Type;

/**
 * Created by rishi on 15-10-29.
 */
public interface DataManager {
    boolean keyExists(DataKey key);
    <T> T getData(DataKey key, Type typeOfT);
    <T> void writeData(DataKey key, T obj, Type typeOfT);
    boolean deleteIfExists(DataKey key);
    boolean isOperational();
}
