/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi
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

import ca.ualberta.cmput301.t03.common.Preconditions;

/**
 * A class that acts as the key mapping to an object being stored by a {@link DataManager}.
 * Behaviourally, this key should correspond to the path where an object is stored. The
 * particulars of the correspondence may be determined by the implementations of {@link DataManager}.
 * Created by rishi on 15-10-29.
 */
public class DataKey {
    private String type;
    private String id;

    /**
     * Creates a new instance of {@link DataKey}
     * @param type A string describing the type of object being stored using this {@link DataKey}.
     * @param id A string describing the unique ID of the object.
     * @throws IllegalArgumentException Thrown, if either of the parameters are null or whitespace.
     */
    public DataKey(String type, String id) throws IllegalArgumentException {
        setType(type);
        setId(id);
    }

    /**
     * Gets the ID for the {@link DataKey}.
     * @return The ID for the {@link DataKey}.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID for the {@link DataKey}.
     * @param id The new ID.
     * @throws IllegalArgumentException Thrown, if the new ID is either null or whitespace.
     */
    public void setId(String id) throws IllegalArgumentException {
        this.id = Preconditions.checkNotNullOrWhitespace(id, "id");
    }

    /**
     * Gets the object type for this {@link DataKey}.
     * @return The object type for this {@link DataKey}.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the object type for this {@link DataKey}.
     * @param type The new object type.
     * @throws IllegalArgumentException Thrown, if the new type is either null or whitespace.
     */
    public void setType(String type) throws IllegalArgumentException {
        this.type = Preconditions.checkNotNullOrWhitespace(type, "type");
    }

    /**
     * Returns a string representation of the {@link DataKey} as a path in the format: type/ID
     * @return The path representation of the {@link DataKey}.
     */
    @Override
    public String toString() {
        return String.format("%s/%s", getType(), getId());
    }
}
