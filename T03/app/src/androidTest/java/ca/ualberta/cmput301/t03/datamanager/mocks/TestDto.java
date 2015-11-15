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

package ca.ualberta.cmput301.t03.datamanager.mocks;

import com.google.gson.annotations.Expose;

/**
 * Created by rishi on 15-10-30.
 */
public class TestDto {
    @Expose
    private int aNumber;
    @Expose
    private String aString;
    @Expose
    private boolean aBoolean;
    private String aHiddenString;


    public TestDto(int aNumber, String aString, boolean aBoolean, String aHiddenString) {
        this.aNumber = aNumber;
        this.aString = aString;
        this.aBoolean = aBoolean;
        this.aHiddenString = aHiddenString;
    }

    public int getaNumber() {
        return aNumber;
    }

    public void setaNumber(int aNumber) {
        this.aNumber = aNumber;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public String getaHiddenString() {
        return aHiddenString;
    }

    public void setaHiddenString(String aHiddenString) {
        this.aHiddenString = aHiddenString;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof TestDto)) {
            return false;
        }

        TestDto rhs = (TestDto) obj;

        return this.getaBoolean() == rhs.getaBoolean() &&
                this.getaHiddenString().equals(rhs.getaHiddenString()) &&
                this.getaNumber() == rhs.getaNumber() &&
                this.getaString().equals(rhs.getaString());
    }

    @Override
    public int hashCode() {
        return new Boolean(this.getaBoolean()).hashCode() ^
                new Integer(this.getaNumber()).hashCode() ^
                this.getaString().hashCode() ^
                this.getaHiddenString().hashCode();
    }
}
