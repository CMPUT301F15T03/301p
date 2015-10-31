package ca.ualberta.cmput301.t03.datamanager;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by rishi on 15-10-29.
 */
public abstract class JsonDataManager implements DataManager {

    protected String serialize(Object obj, Type type) {
        return serialize(obj, type, new JsonFormatter(false, true));
    }

    protected String serialize(Object obj, Type type, JsonFormatter format) {
        Gson gson = format.getGson();
        return gson.toJson(obj, type);
    }

    protected <T> T deserialize(String obj, Type typeOfT) {
        return deserialize(obj, typeOfT, new JsonFormatter(false, true));
    }

    protected <T> T deserialize(String obj, Type typeOfT, JsonFormatter format) {
        Gson gson = format.getGson();
        return gson.fromJson(obj, typeOfT);
    }
}
