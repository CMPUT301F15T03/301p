/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
