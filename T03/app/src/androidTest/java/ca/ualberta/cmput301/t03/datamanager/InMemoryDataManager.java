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
import java.util.HashMap;

/**
 * Created by rishi on 15-11-08.
 */
public class InMemoryDataManager extends JsonDataManager {

    private boolean isOperational = true;
    private HashMap<String, Object> inMemoryDataRepository = new HashMap<>();

    @Override
    public boolean keyExists(DataKey key) throws IOException {
        return inMemoryDataRepository.containsKey(key.toString());
    }

    @Override
    public <T> T getData(DataKey key, Type typeOfT) throws IOException {
        return (T) inMemoryDataRepository.get(key.toString());
    }

    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException {
        inMemoryDataRepository.put(key.toString(), obj);
    }

    @Override
    public boolean deleteIfExists(DataKey key) throws IOException {
        return inMemoryDataRepository.remove(key.toString()) != null;
    }

    @Override
    public boolean isOperational() {
        return isOperational;
    }

    public void setIsOperational(boolean isOperational) {
        this.isOperational = isOperational;
    }
}
