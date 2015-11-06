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

import java.util.ArrayList;
import java.util.Collection;

import ca.ualberta.cmput301.t03.Observer;

import java.util.Collection;

/**
 * Created by mmabuyo on 2015-10-29 .
 */
public class PhotoGallery {
    private Collection<Observer> observers;
    private Photo photoInFocus;
    private ArrayList<Photo> photos;

    /**
     * Gets photo that is in current full-view of the phone's screen.
     * @return
     */
    public Photo getPhotoInFocus() {
        return photoInFocus;
    }

    /**
     * Sets photo in focus to be current full-view of the phone's screen.
     * @param photoInFocus
     */
    public void setPhotoInFocus(Photo photoInFocus) {
        this.photoInFocus = photoInFocus;
    }

    /**
     * Retrieves the list of photos for the item.
     * @return Array list of item's photos.
     */
    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    /**
     * Sets the given array list of photos to be the item's photos.
     * @param photos
     */
    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public void load() { throw new UnsupportedOperationException(); }
    public void save() { throw new UnsupportedOperationException(); }

    /**
     * Removes the photo from the item's photo gallery.
     * @param photo
     */
    public void removePhoto(Photo photo) { throw new UnsupportedOperationException(); }

    /**
     * Adds the photo to the item's photo gallery providing it meets the photo limit for an item.
     * @param photo
     */
    public void addPhoto(Photo photo) { throw new UnsupportedOperationException(); }


}
