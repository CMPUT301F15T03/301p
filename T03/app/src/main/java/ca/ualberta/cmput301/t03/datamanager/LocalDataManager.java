package ca.ualberta.cmput301.t03.datamanager;

import android.content.Context;

import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;

/**
 * Created by rishi on 15-10-30.
 */
public class LocalDataManager implements DataManager {

    private final Context context;

    public LocalDataManager(Context context) {
        this.context = Preconditions.checkNotNull(context, "context");
    }

    @Override
    public boolean keyExists(DataKey key) {
        throw new NotImplementedException();
    }

    @Override
    public <T> T getData(DataKey key, Type typeOfT) {
        throw new NotImplementedException();
    }

    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(DataKey key) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isOperational() {
        throw new NotImplementedException();
    }
}
