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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Scanner;

import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;

/**
 * A {@link JsonDataManager} that stores the serialized JSONs locally in the phone's storage.
 * Created by rishi on 15-10-30.
 */
public class LocalDataManager extends JsonDataManager {

    /**
     * Creates an instance of the {@link LocalDataManager}.
     *
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public LocalDataManager(boolean useExplicitExposeAnnotation) {
        super(useExplicitExposeAnnotation);
    }

    /**
     * Creates an instance of the {@link LocalDataManager}. The manager will set the value of
     * "useExplicitExposeAnnotation" to false.
     */
    public LocalDataManager() {
        this(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyExists(DataKey key) {
        Preconditions.checkNotNull(key, "key");
        return getTargetFile(key, false).exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getData(DataKey key, Type typeOfT) {
        Preconditions.checkNotNull(key, "key");

        if (!keyExists(key)) {
            throw new DataKeyNotFoundException(key);
        }

        File targetFile = getTargetFile(key, false);

        Scanner scanner;
        try {
            scanner = new Scanner(targetFile);
        } catch (FileNotFoundException e) {
            throw new NotImplementedException("Dev note: This exception shouldn't have been thrown, as the necessary dirs are created.", e);
        }

        String fileContents = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return deserialize(fileContents, typeOfT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) {
        Preconditions.checkNotNull(key, "key");
        Preconditions.checkNotNull(obj, "obj");

        String json = serialize(obj, typeOfT);
        File targetFile = getTargetFile(key, true);
        PrintWriter out;

        try {
            out = new PrintWriter(new FileOutputStream(targetFile, false));
        } catch (FileNotFoundException e) {
            throw new NotImplementedException("Dev note: This exception shouldn't have been thrown, as the necessary dirs are created.", e);
        }

        out.print(json);
        out.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteIfExists(DataKey key) {
        if (!keyExists(key)) {
            return;
        }

        File targetFile = getTargetFile(key, false);
        targetFile.delete();
    }

    /**
     * Not applicable for LocalDataManager. Always returns true.
     *
     * @return Always true.
     */
    @Override
    public boolean isOperational() {
        return true;
    }

    /**
     * Return false.
     */
    @Override
    public boolean requiresNetwork() {
        return false;
    }

    // Source: http://stackoverflow.com/questions/2130932/how-to-create-directory-automatically-on-sd-card
    // Date: 30 Oct, 2015
    private File getTargetFile(DataKey key, boolean createTypeDirectoryIfNotExists) {
        File appContextRootDirectory = TradeApp.getContext().getFilesDir();
        File typeDirectory = new File(appContextRootDirectory, key.getType());
        if (createTypeDirectoryIfNotExists && !typeDirectory.exists()) {
            typeDirectory.mkdirs();
        }
        return new File(typeDirectory, key.getId() + ".json");
    }
}
