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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.path.android.jobqueue.JobManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.QueuedDataManager;

/**
 * Object for the item's photo. Belongs to an item's PhotoGallery.
 */
public class Photo implements Observable {

    public static final String type = "Photo";
    @Expose
    private UUID photoUUID; // instead of a Uri location ie, on elastic search we would have ...to3/Photo/{UUID}
    private Collection<Observer> observers;
    private DataManager dataManager;


    private Base64Wrapper base64Photo;
    private boolean isDownloaded;

    public Photo() {
        isDownloaded = false;
        observers = new ArrayList<>();
        dataManager = TradeApp.getInstance().createDataManager(false);
        base64Photo = new Base64Wrapper();
    }

    public Photo(Bitmap photo) {
        isDownloaded = false;
        observers = new ArrayList<>();
        dataManager = TradeApp.getInstance().createDataManager(false);
        photoUUID = UUID.randomUUID();
        base64Photo = new Base64Wrapper();
        setPhoto(photo);
    }

    /**
     * Determines if the photo has been downloaded on the phone.
     *
     * @return true if the photo has been downloaded, false if not
     */
    public boolean isDownloaded() {
        return this.isDownloaded;
    }

    /**
     * Starts downloading the photo.
     *
     * For an ImageView, call setBitmap(Photo.getPhoto()); -- i think
     */
    public Bitmap getPhoto() {
        if (!isDownloaded) {
            try {
                base64Photo = dataManager.getData(new DataKey(Photo.type, photoUUID.toString()), Base64Wrapper.class);
                isDownloaded = true;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        byte[] bytes = Base64.decode(base64Photo.getContents(), Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void setPhoto(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        base64Photo.setContents(Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP));
        isDownloaded = true;

        try {
            dataManager.writeData(new DataKey(Photo.type, photoUUID.toString()), base64Photo, Base64Wrapper.class);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePhoto() {
        try {
            dataManager.deleteIfExists(new DataKey(Photo.type, photoUUID.toString()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void clearObservers() {
        observers.clear();
    }

    public UUID getPhotoUUID() {
        return photoUUID;
    }

    public void setPhotoUUID(UUID photoUUID) {
        this.photoUUID = photoUUID;
    }

    public Base64Wrapper getBase64Photo() {
        return base64Photo;
    }

    public void setBase64Photo(Base64Wrapper base64Photo) {
        this.base64Photo = base64Photo;
    }
}