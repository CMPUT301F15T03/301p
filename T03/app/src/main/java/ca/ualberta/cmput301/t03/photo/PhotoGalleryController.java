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
 * Used by the PhotoGalleryView to handle edits to the model.
 */
public class PhotoGalleryController {
    private PhotoGallery model;

    /**
     * Initializes the controller with the photoGallery model and creates an instance of a controller
     * This controller is user by the photoGalleryView to handle any edit operations to the model
     *
     * @param model
     */
    public PhotoGalleryController(PhotoGallery model) {
        this.model = model;
    }

    public void removePhoto(Integer index) {
        model.removePhoto(model.getPhotos().get(index));
    }
}
