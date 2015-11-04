package ca.ualberta.cmput301.t03.common.http;

import com.google.gson.annotations.SerializedName;

/**
 * A model for serializing the responses from ElasticSearch server. All fields, and getter/setter
 * methods correspond to the fields in response JSON.
 * Created by rishi on 15-10-31.
 */
public class ElasticSearchResponse<T> {

    @SerializedName("_index")
    private String index;

    @SerializedName("_type")
    private String type;

    @SerializedName("_id")
    private String id;

    @SerializedName("_version")
    private String version;

    @SerializedName("found")
    private boolean isFound;

    @SerializedName("created")
    private boolean isCreated;

    @SerializedName("_source")
    private T source;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean getIsFound() {
        return isFound;
    }

    public void setIsFound(boolean isFound) {
        this.isFound = isFound;
    }

    public boolean getIsCreated() {
        return isCreated;
    }

    public void setIsCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }
}
