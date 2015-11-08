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

package ca.ualberta.cmput301.t03;

/**
 * The data object that is observed in the Observer Pattern. Responsible for triggering update() on
 * its Observers and maintaining a list of those Observers.
 */
public interface Observable {
    /**
     * Called when state of the Observable should be noticed by the Observers.
     */
    void notifyObservers();

    /**
     * Add an Observer to a collection of Observers that should be updated upon a notify.
     *
     * @param observer the Observer to add
     */
    void addObserver(Observer observer);

    /**
     * Remove an Observer from maintained collection of Observers. This Observer should no longer
     * be notified of changes.
     *
     * @param observer the Observer to remove
     */
    void removeObserver(Observer observer);

    /**
     * Remove all Observers from maintained collection of Observers. These Observers should no longer
     * be notified of changes.
     */
    void clearObservers();

}
