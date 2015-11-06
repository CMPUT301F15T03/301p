/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of {ApplicationName}
 *
 * {ApplicationName} is free software: you can redistribute it and/or modify
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

package ca.ualberta.cmput301.t03.datamanager;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * An abstract implementation of {@link DataManager} that stores the objects as JSONs to a storage
 * media.
 * Created by rishi on 15-10-29.
 */
public abstract class JsonDataManager implements DataManager {

    protected final JsonFormatter jsonFormatter;

    /**
     * Creates an instance of {@link JsonDataManager}.
     *
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public JsonDataManager(boolean useExplicitExposeAnnotation) {
        this.jsonFormatter = new JsonFormatter(useExplicitExposeAnnotation, true);
    }

    /**
     * Creates an instance of {@link JsonDataManager}. The manager will set the "useExplicitExposeAnnotation"
     * value to false.
     */
    public JsonDataManager() {
        this(false);
    }

    /**
     * Serializes the passed object to a JSON string. A {@link JsonFormatter} with "useExplicitExposeAnnotation"
     * set to the construction time value, and "usePrettyJson" value set to true will be used.
     *
     * @param obj  The object to be serialized.
     * @param type The runtime {@link Type} of the object.
     * @return The serialized JSON.
     */
    protected String serialize(Object obj, Type type) {
        return serialize(obj, type, jsonFormatter);
    }

    /**
     * Serializes the passed object to a JSON string.
     *
     * @param obj    The object to be serialized.
     * @param type   The runtime {@link Type} of the object.
     * @param format The {@link JsonFormatter} to be used for serialization.
     * @return The serialized JSON.
     */
    protected String serialize(Object obj, Type type, JsonFormatter format) {
        Gson gson = format.getGson();
        return gson.toJson(obj, type);
    }

    /**
     * De-serializes the JSON to a Java object. A {@link JsonFormatter} with "useExplicitExposeAnnotation"
     * set to the construction time value, and "usePrettyJson" value set to true will be used.
     *
     * @param obj     The JSON string.
     * @param typeOfT The Java object's {@link Type}
     * @param <T>     The generic type param corresponding to the parameter "typeOfT"
     * @return The de-serialized object.
     */
    protected <T> T deserialize(String obj, Type typeOfT) {
        return deserialize(obj, typeOfT, jsonFormatter);
    }

    /**
     * De-serializes the JSON to a Java object.
     *
     * @param obj     The JSON string.
     * @param typeOfT The Java object's {@link Type}
     * @param format  The {@link JsonFormatter} to be used for de-serialization.
     * @param <T>     The generic type param corresponding to the parameter "typeOfT"
     * @return The de-serialized object.
     */
    protected <T> T deserialize(String obj, Type typeOfT, JsonFormatter format) {
        Gson gson = format.getGson();
        return gson.fromJson(obj, typeOfT);
    }
}
