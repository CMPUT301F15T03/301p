package ca.ualberta.cmput301.t03.datamanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by rishi on 15-10-30.
 */
public class JsonFormatter {
    private boolean useExplicitExposeAnnotation;
    private boolean usePrettyJson;

    public JsonFormatter(boolean useExplicitExposeAnnotation, boolean usePrettyJson) {
        setUseExplicitExposeAnnotation(useExplicitExposeAnnotation);
        setUsePrettyJson(usePrettyJson);
    }

    public boolean getUseExplicitExposeAnnotation() {
        return useExplicitExposeAnnotation;
    }

    public void setUseExplicitExposeAnnotation(boolean useExplicitExposeAnnotation) {
        this.useExplicitExposeAnnotation = useExplicitExposeAnnotation;
    }

    public boolean getUsePrettyJson() {
        return usePrettyJson;
    }

    public void setUsePrettyJson(boolean usePrettyJson) {
        this.usePrettyJson = usePrettyJson;
    }

    public Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        if (getUsePrettyJson()) {
            gsonBuilder.setPrettyPrinting();
        }

        if (getUseExplicitExposeAnnotation()) {
            gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        }

        return gsonBuilder.create();
    }
}
