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

/**
 * Controls the photo gallery for an item.
 */
public class PhotoGalleryController {
    private PhotoGallery photoGalleryModel;

    public PhotoGalleryController(PhotoGallery photoGalleryModel) {
        this.photoGalleryModel = photoGalleryModel;
    }

    /**
     * Shows user the next photo in the photo gallery in full view, if there is one.
     */
    public void swipeLeft() { throw new UnsupportedOperationException(); }

    /**
     * Shows user the previous photo in the photo gallery in full view, if there is one.
     */
    public void swipeRight() { throw new UnsupportedOperationException(); }

    /**
     * Brings up the menu if the user wants to download the photo.
     */
    public void longClick() { throw new UnsupportedOperationException(); }

    /**
     * Exits PhotoGallery view (no more full screen photo)
     */
    public void swipeDown() { throw new UnsupportedOperationException(); }
}
