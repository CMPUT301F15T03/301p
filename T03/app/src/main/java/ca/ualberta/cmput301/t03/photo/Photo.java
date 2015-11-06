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

package ca.ualberta.cmput301.t03.photo;

import android.net.Uri;

import java.util.Collection;

import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.datamanager.DataManager;

import android.net.Uri;
import java.util.Collection;

/**
 * Object for the item's photo. Belongs to an item's PhotoGallery.
 */
public class Photo {
    private Uri location;
    private DataManager dataManager;
    private Collection<Observer> observers;
    private boolean isDownloaded;

    public Photo() {

    }

    /**
     * Determines if the photo has been downloaded on the phone.
     * @return true if the photo has been downloaded, false if not
     */
    public boolean isDownloaded() {
        return this.isDownloaded;
    }

    /**
     * Starts downloading the photo.
     */
    public void downloadPhoto() {
        throw new UnsupportedOperationException();
    }

    public void load() { throw new UnsupportedOperationException(); }
    public void save() { throw new UnsupportedOperationException(); }
}