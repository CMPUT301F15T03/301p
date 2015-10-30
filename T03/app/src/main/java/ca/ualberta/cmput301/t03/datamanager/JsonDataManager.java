package ca.ualberta.cmput301.t03.datamanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by rishi on 15-10-29.
 */
public abstract class JsonDataManager implements DataManager {

    private final Gson gson;

    public JsonDataManager() {
        // Source: http://www.mkyong.com/java/how-to-enable-pretty-print-json-output-gson/
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    protected String serialize(Object obj, Type type) {
        return gson.toJson(obj, type);
    }

    protected <T> T deserialize(String obj, Type typeOfT) {
        return gson.fromJson(obj, typeOfT);
    }
}
