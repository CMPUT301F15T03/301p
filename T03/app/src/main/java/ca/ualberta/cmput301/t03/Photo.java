package ca.ualberta.cmput301.t03;

import android.net.Uri;
import java.util.Collection;

/**
 * Created by mmabuyo on 2015-10-29.
 */
public class Photo {
    private Uri location;
    private DataManager dataManager;
    private Collection<Observer> observers;

    public void load() { throw new UnsupportedOperationException(); }
    public void save() { throw new UnsupportedOperationException(); }
}