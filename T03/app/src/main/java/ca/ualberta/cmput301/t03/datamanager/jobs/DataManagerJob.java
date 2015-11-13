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

package ca.ualberta.cmput301.t03.datamanager.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.datamanager.DataKey;

/**
 * Created by rishi on 15-11-11.
 */
public abstract class DataManagerJob extends Job {
    private static final int PRIORITY = 1000;
    private static final String GROUP = "datamanagerjob";

    private final String type;
    private final String id;

    public DataManagerJob(DataKey dataKey) {
        super(new Params(PRIORITY).persist().requireNetwork().groupBy(GROUP));
        Preconditions.checkNotNull(dataKey, "dataKey");
        this.type = dataKey.getType();
        this.id = dataKey.getId();
    }

    @Override
    public void onAdded() { }

    @Override
    protected void onCancel() {
        throw new NotImplementedException();
    }

    // Needed because only simple types can be serialized
    protected String getRequestSuffix() {
        return new DataKey(type, id).toString();
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }
}
