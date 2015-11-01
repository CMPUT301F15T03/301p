package ca.ualberta.cmput301.t03.datamanager;

import ca.ualberta.cmput301.t03.common.Preconditions;

/**
 * Created by rishi on 15-10-29.
 */
public class DataKey {
    private String type;
    private String id;

    public DataKey(String type, String id) throws IllegalArgumentException {
        setType(type);
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) throws IllegalArgumentException {
        this.id = Preconditions.checkNotNullOrWhitespace(id, "id");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) throws IllegalArgumentException {
        this.type = Preconditions.checkNotNullOrWhitespace(type, "type");
    }

    @Override
    public String toString() {
        return String.format("%s/%s", getType(), getId());
    }
}
