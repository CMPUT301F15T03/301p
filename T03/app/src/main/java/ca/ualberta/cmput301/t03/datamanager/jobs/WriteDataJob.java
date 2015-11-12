package ca.ualberta.cmput301.t03.datamanager.jobs;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;

/**
 * Created by rishi on 15-11-11.
 */
public class WriteDataJob extends DataManagerJob {

    private final Object obj;
    private final Type type;
    private final boolean useExplicitExposeAnnotation;

    public WriteDataJob(DataKey dataKey,
                        Object obj,
                        Type typeOfT,
                        boolean useExplicitExposeAnnotation,
                        OnRequestQueuedCallback onRequestQueuedCallback) {
        super(dataKey, onRequestQueuedCallback);
        this.obj = Preconditions.checkNotNull(obj, "obj");
        this.type = Preconditions.checkNotNull(typeOfT, "typeOfT");
        this.useExplicitExposeAnnotation = useExplicitExposeAnnotation;
    }

    public WriteDataJob(DataKey dataKey,
                        Objects obj,
                        Type typeOfT,
                        boolean useExplicitExposeAnnotation) {
        this(dataKey, obj, typeOfT, useExplicitExposeAnnotation, null);
    }

    @Override
    public void onRun() throws IOException {
        new HttpDataManager(useExplicitExposeAnnotation).writeData(getDataKey(), obj, type);
    }
}
