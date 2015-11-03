package ca.ualberta.cmput301.t03.datamanager;

import android.content.res.Resources;

/**
 * Subclass of {@link android.content.res.Resources.NotFoundException} thrown when a particular
 * {@link DataKey} is not found by a {@link DataManager}.
 * Created by rishi on 15-10-30.
 */
public class DataKeyNotFoundException extends Resources.NotFoundException {
    /**
     * Creates a new instance of {@link DataKeyNotFoundException}.
     */
    public DataKeyNotFoundException() {
        super();
    }

    /**
     * Creates a new instance of {@link DataKeyNotFoundException}.
     * @param name The name of the {@link DataKey} not found.
     */
    public DataKeyNotFoundException(String name) {
        super(name);
    }
}
