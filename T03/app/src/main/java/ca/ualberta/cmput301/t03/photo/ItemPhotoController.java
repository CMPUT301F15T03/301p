/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
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

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import ca.ualberta.cmput301.t03.inventory.Item;

/**
 * ItemPhotoController is the controller used in editItemView and AddItemView to aid in processing
 * new photos for an item. This controller contains the logic for reducing the size of photos
 * to the TARGET_IMAGE_SIZE_BYTES target.
 */
public class ItemPhotoController {

    static int TARGET_IMAGE_SIZE_BYTES = 65536;

    private Item itemModel;

    public ItemPhotoController(Item itemModel) {
        this.itemModel = itemModel;
    }

    /**
     * Add a bitmap image (resized to a target size of 64k) to the model item's photoGallery
     * This is called by the edit item view
     *
     * @param image
     */
    public void addPhotoToItem(Bitmap image) {
        Photo photo = new Photo(resizeImage(image, TARGET_IMAGE_SIZE_BYTES));
        itemModel.getPhotoList().addPhoto(photo);
    }

    /**
     * Given a bitmap, return a new bitmap that has been sized down to fit the targetSize constraint
     * in bytes.
     *
     * @param image      the image to be down-sized
     * @param targetSize the target size in bytes for the passed image
     * @return The new bitmap, resized to be smaller or equal to the target size
     */
    private Bitmap resizeImage(Bitmap image, long targetSize) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        long currentImageSize = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP).length();

        while (currentImageSize > targetSize) {
            image = Bitmap.createScaledBitmap(image, image.getWidth() / 2, image.getHeight() / 2, false);
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            currentImageSize = Base64.encodeToString(stream2.toByteArray(), Base64.NO_WRAP).length();
        }

        return image;

    }

}
