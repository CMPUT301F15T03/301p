package ca.ualberta.cmput301.t03.datamanager;

/**
 * Created by rishi on 15-10-30.
 */
public class JsonFormatter {

    private boolean useExplicitExposeAnnotation;

    public JsonFormatter(boolean useExplicitExposeAnnotation) {
        setUseExplicitExposeAnnotation(useExplicitExposeAnnotation);
    }

    public boolean useExplicitExposeAnnotation() {
        return useExplicitExposeAnnotation;
    }

    public void setUseExplicitExposeAnnotation(boolean useExplicitExposeAnnotation) {
        this.useExplicitExposeAnnotation = useExplicitExposeAnnotation;
    }
}
