package ca.ualberta.cmput301.t03.datamanager;

import android.content.res.Resources;

/**
 * Created by rishi on 15-10-30.
 */
public class DataKeyNotFoundException extends Resources.NotFoundException {
    public DataKeyNotFoundException() {
    }

    public DataKeyNotFoundException(String name) {
        super(name);
    }
}
