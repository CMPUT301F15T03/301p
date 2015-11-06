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

package ca.ualberta.cmput301.t03.user;

import com.google.gson.annotations.Expose;

import java.util.HashSet;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;

/**
 * Model that represents a given User's profile information.
 * Contains various information, including contact information.
 * Can be modified by the Application User at any time.
 * <p>
 * Will notify the parent user of an changes made
 */
public class UserProfile implements Observable {
    public final static String type = "UserProfile";
    @Expose
    private String city;
    @Expose
    private String email;
    @Expose
    private String phone;

    private HashSet<Observer> observers;

    public UserProfile() {
        observers = new HashSet<>();
    }

    /**
     * Get the profile's city.
     *
     * @return profile's city
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the profile's city.
     *
     * @param city new value for city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get the profile's email.
     *
     * @return profile's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the profile's email.
     *
     * @param email new value for email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the profile's phone number.
     *
     * @return profile's phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set the profile's phone number.
     *
     * @param phone the new value for phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Alias for notifyObservers to be called after any edits have been made to the object.
     */
    public void commitChanges() {
        notifyObservers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
