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

import android.test.AndroidTestCase;

import com.google.gson.reflect.TypeToken;

import junit.framework.AssertionFailedError;

import java.io.IOException;

import ca.ualberta.cmput301.t03.common.exceptions.ExceptionUtils;

import static ca.ualberta.cmput301.t03.commontesting.ExceptionAsserter.assertThrowsException;

/**
 * Created by rishi on 15-10-30.
 */
// Source: http://developer.android.com/reference/android/test/AndroidTestCase.html
// Date: 30-Oct-15
public abstract class BaseDataManagerTests<T extends DataManager> extends AndroidTestCase {
    protected T dataManager;
    protected TestDto testDto;
    protected DataKey dataKey;

    protected abstract T createNewDataManager();

    public void setUp() {
        dataManager = createNewDataManager();
        testDto = new TestDto(100, "Hundred", false, "a hidden string");
        dataKey = new DataKey("testdto", "123");
    }

    protected void keyExistsTest() {
        try {
            assertFalse(dataManager.keyExists(dataKey));
            dataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {
            }.getType());
            assertTrue(dataManager.keyExists(dataKey));
            assertFalse(dataManager.keyExists(new DataKey("not", "exists")));
            assertTrue(dataManager.deleteIfExists(dataKey));
        }
        catch (IOException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        }
    }

    protected void getDataWhenKeyDoesNotExistThrowsExceptionTest() {
        try {
            assertFalse(dataManager.keyExists(dataKey));
            assertThrowsException(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataManager.getData(dataKey, new TypeToken<TestDto>() {
                        }.getType());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, DataKeyNotFoundException.class);
        }
        catch (IOException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        }
    }

    protected void writeDataTest() {
        try {
            dataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {
            }.getType());
            assertTrue(dataManager.keyExists(dataKey));
            TestDto receivedData = dataManager.getData(dataKey, new TypeToken<TestDto>() {
            }.getType());
            assertEquals(testDto, receivedData);
            assertTrue(dataManager.deleteIfExists(dataKey));
        }
        catch (IOException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        }
    }

    protected void deleteTest() {
        try {
            assertFalse(dataManager.keyExists(dataKey));
            assertFalse(dataManager.deleteIfExists(dataKey));
            dataManager.writeData(dataKey, testDto, new TypeToken<TestDto>() {
            }.getType());
            assertTrue(dataManager.keyExists(dataKey));
            assertTrue(dataManager.deleteIfExists(dataKey));
            assertFalse(dataManager.keyExists(dataKey));
        }
        catch (IOException e) {
            throw new AssertionFailedError(e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));
        }
    }

    protected void isOperationalTest() {
        assertTrue(dataManager.isOperational());
    }
}
