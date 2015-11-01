package ca.ualberta.cmput301.t03.datamanager.httpdatamanager;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.JsonDataManager;

/**
 * Created by rishi on 15-10-30.
 */
public class HttpDataManager extends JsonDataManager {

    private final URL rootUrl;

    public HttpDataManager(String rootUrl) throws MalformedURLException {
        this.rootUrl = new URL(Preconditions.checkNotNullOrWhitespace(rootUrl, "rootUrl"));
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
    public boolean deleteIfExists(DataKey key) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isOperational() {
        throw new NotImplementedException();
    }

    private URL getQueryUrl(DataKey key) {
        try {
            return new URL(new URL(rootUrl, key.getType()), key.getId());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("The DataKey cannot be converted to a valid URL.", e);
        }
    }
}
