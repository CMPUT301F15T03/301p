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


import com.path.android.jobqueue.JobManager;

import java.io.IOException;
import java.lang.reflect.Type;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.datamanager.jobs.DeleteDataJob;
import ca.ualberta.cmput301.t03.datamanager.jobs.WriteDataJob;

/**
 * A {@link CachedDataManager} that uses {@link JobManager} for queuing up the the write and deletion
 * operations. It uses {@link HttpDataManager} as the inner manager.
 *
 * It guarantees:
 * 1. All the write and delete operations will eventually go through, no matter what the current
 *    internet state is. The requests are persisted, so that they are still sent if the app restarts.
 * 2. All the operations will be sent in the exact same order as they were queued. This guarantees
 *    data integrity for operations that work on the same remote object.
 * 3. All the operations will be performed on a different thread. So these methods can be called on
 *    the UI thread safely, even though network is accessed.
 * Created by rishi on 15-11-11.
 */
public class QueuedDataManager extends CachedDataManager {

    private final JobManager jobManager;

    /**
     * Creates an instance of the {@link QueuedDataManager}.
     * @param jobManager The {@link JobManager} to be used for queueing jobs.
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public QueuedDataManager(JobManager jobManager, boolean useExplicitExposeAnnotation) {
        this(jobManager, new HttpDataManager(useExplicitExposeAnnotation));
    }

    /**
     * Creates an instance of the {@link QueuedDataManager}. Sets the "useExplicitExposeAnnotation"
     * value to false.
     * @param jobManager The {@link JobManager} to be used for queueing jobs.
     */
    public QueuedDataManager(JobManager jobManager) {
        this(jobManager, false);
    }

    /**
     * Test only constructor for working with a test version of {@link HttpDataManager}/
     * @param jobManager The {@link JobManager} to be used for queueing jobs.
     * @param innerManager The {@link HttpDataManager} to be used as the inner manager.
     */
    protected QueuedDataManager(JobManager jobManager, HttpDataManager innerManager) {
        super(Preconditions.checkNotNull(innerManager, "innerManager"));
        this.jobManager = Preconditions.checkNotNull(jobManager, "jobManager");
    }

    /**
     * Creates a job for writing the data to the location pointed by the key, and queues it up.
     * @param key     The {@link DataKey} for the object.
     * @param obj     The object to be stored.
     * @param typeOfT The {@link Type} of the object.
     * @param <T>     The type of the object.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    @Override
    public <T> void writeData(final DataKey key, final T obj, final Type typeOfT) throws IOException {
        String json = serialize(obj, typeOfT);
        WriteDataJob job = new WriteDataJob(key, json);
        jobManager.addJobInBackground(job);
        writeToCache(key, obj, typeOfT);
    }

    /**
     * Creates a job for deleting the data at the location pointed by the key, and queues it up.
     * @param key The {@link DataKey} for which the object has to be deleted.
     * @throws IOException Thrown, if the communication to the storage media fails.
     */
    @Override
    public void deleteIfExists(final DataKey key) throws IOException {
        DeleteDataJob job = new DeleteDataJob(key);
        jobManager.addJobInBackground(job);
        deleteFromCache(key);
    }

    /**
     * Tells if the data manager is operational or not.
     * @return Always true, as the manager is always operational, either through cache or through queuing.
     */
    @Override
    public boolean isOperational() {
        return true;
    }
}
