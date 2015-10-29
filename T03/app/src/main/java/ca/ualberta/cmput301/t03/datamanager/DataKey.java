package ca.ualberta.cmput301.t03.datamanager;

/**
 * Created by rishi on 15-10-29.
 */
public class DataKey {
    private String type;
    private String id;

    public DataKey(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s/%s", getType(), getId());
    }
}
