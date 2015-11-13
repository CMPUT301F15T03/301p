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
 * Created by rishi on 15-11-11.
 */
public class QueuedDataManager extends CachedDataManager {

    private final JobManager jobManager;

    public QueuedDataManager(JobManager jobManager, boolean useExplicitExposeAnnotation) {
        this(jobManager, new HttpDataManager(useExplicitExposeAnnotation));
    }

    public QueuedDataManager(JobManager jobManager) {
        this(jobManager, false);
    }

    protected QueuedDataManager(JobManager jobManager, HttpDataManager innerManager) {
        super(Preconditions.checkNotNull(innerManager, "innerManager"));
        this.jobManager = Preconditions.checkNotNull(jobManager, "jobManager");
    }

    @Override
    public <T> void writeData(final DataKey key, final T obj, final Type typeOfT) throws IOException {
        String json = serialize(obj, typeOfT);
        WriteDataJob job = new WriteDataJob(key, json);
        jobManager.addJobInBackground(job);
        writeToCache(key, obj, typeOfT);
    }

    @Override
    public void deleteIfExists(final DataKey key) throws IOException {
        DeleteDataJob job = new DeleteDataJob(key);
        jobManager.addJobInBackground(job);
        deleteFromCache(key);
    }

    @Override
    public boolean isOperational() {
        return true;
    }
}
