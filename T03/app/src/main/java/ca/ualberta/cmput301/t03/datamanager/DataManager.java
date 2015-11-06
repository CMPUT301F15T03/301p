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

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * A base interface for a class acting as a data manager for arbitrary types of objects.
 * The implementations work with {@link DataKey} to uniquely map the objects to paths on whatever
 * storage media they are using.
 * Created by rishi on 15-10-29.
 */
public interface DataManager {
    /**
     * Checks if the {@link DataKey} exists in the storage media.
     *
     * @param key The {@link DataKey} to be looked up.
     * @return True, if the key exists, else false.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    boolean keyExists(DataKey key) throws IOException;

    /**
     * Retrieves the data pointed to by the key from the storage media.
     *
     * @param key     The {@link DataKey} to be used for lookup.
     * @param typeOfT The {@link Type} of the object being retrieved.
     * @param <T>     The type of the object being retrieved. This type should match with the parameter
     *                "typeOfT", else a {@link ClassCastException} might be thrown.
     * @return The retrieved object.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    <T> T getData(DataKey key, Type typeOfT) throws IOException;

    /**
     * Write the object to the storage, or overwrites the existing one if one existed.
     *
     * @param key     The {@link DataKey} for the object.
     * @param obj     The object to be stored.
     * @param typeOfT The {@link Type} of the object.
     * @param <T>     The type of the object.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException;

    /**
     * Deletes the object pointed by the {@link DataKey} from the storage media, if it exists.
     *
     * @param key The {@link DataKey} for which the object has to be deleted.
     * @return True, if the object was found and was deleted. False, if the object was not found.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    boolean deleteIfExists(DataKey key) throws IOException;

    /**
     * Checks if the storage media used by this {@link DataManager} implementation is currently
     * operational or online.
     *
     * @return True, if the {@link DataManager} is completely operational, else false. Some
     * operations might still work if false is returned.
     */
    boolean isOperational();
}
