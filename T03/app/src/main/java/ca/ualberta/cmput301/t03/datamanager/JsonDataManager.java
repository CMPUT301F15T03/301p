package ca.ualberta.cmput301.t03.datamanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by rishi on 15-10-29.
 */
public abstract class JsonDataManager implements DataManager {

    private static final boolean USE_PRETTY_JSON = true;

    protected String serialize(Object obj, Type type) {
        return serialize(obj, type, null);
    }

    protected String serialize(Object obj, Type type, JsonFormatter format) {
        Gson gson = getGsonFromJsonFormatter(format);
        return gson.toJson(obj, type);
    }

    protected <T> T deserialize(String obj, Type typeOfT) {
        return deserialize(obj, typeOfT, null);
    }

    protected <T> T deserialize(String obj, Type typeOfT, JsonFormatter format) {
        Gson gson = getGsonFromJsonFormatter(format);
        return gson.fromJson(obj, typeOfT);
    }

    private Gson getGsonFromJsonFormatter(JsonFormatter format) {
        if (format == null) {
            format = new JsonFormatter(false);
        }

        GsonBuilder gsonBuilder = new GsonBuilder();

        if (USE_PRETTY_JSON) {
            gsonBuilder.setPrettyPrinting();
        }

        if (format.useExplicitExposeAnnotation) {
            gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        }

        return gsonBuilder.create();
    }

    public static class JsonFormatter {

        private boolean useExplicitExposeAnnotation;

        public JsonFormatter(boolean useExplicitExposeAnnotation) {
            setUseExplicitExposeAnnotation(useExplicitExposeAnnotation);
        }

        public void setUseExplicitExposeAnnotation(boolean useExplicitExposeAnnotation) {
            this.useExplicitExposeAnnotation = useExplicitExposeAnnotation;
        }
    }
}
