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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A class for specifying the JSON format the {@link JsonDataManager} should use.
 * Created by rishi on 15-10-30.
 */
public class JsonFormatter {
    private boolean useExplicitExposeAnnotation;
    private boolean usePrettyJson;

    /**
     * Creates an instance of the {@link JsonFormatter}.
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     * @param usePrettyJson True, if the serialized JSON needs to be in "pretty" format (with
     *                      appropriate newlines and indentations). False, if the JSON needs to be
     *                      in a single line.
     */
    public JsonFormatter(boolean useExplicitExposeAnnotation, boolean usePrettyJson) {
        setUseExplicitExposeAnnotation(useExplicitExposeAnnotation);
        setUsePrettyJson(usePrettyJson);
    }

    /**
     * Gets the {@link JsonFormatter}'s current "use explicit expose annotation" setting. If this is
     * set to true, only the fields with the annotation @expose will be serialized/de-serialized.
     * @return The current "use explicit expose annotation" setting.
     */
    public boolean getUseExplicitExposeAnnotation() {
        return useExplicitExposeAnnotation;
    }

    /**
     * Sets the {@link JsonFormatter}'s current "use explicit expose annotation" setting. If this is
     * set to true, only the fields with the annotation @expose will be serialized/de-serialized.
     * @param useExplicitExposeAnnotation The new "use explicit expose annotation" setting.
     */
    public void setUseExplicitExposeAnnotation(boolean useExplicitExposeAnnotation) {
        this.useExplicitExposeAnnotation = useExplicitExposeAnnotation;
    }

    /**
     * Gets the {@link JsonFormatter}'s current "use pretty JSON" setting.
     * @return True, if the serialized JSON needs to be in "pretty" format (with appropriate
     *         newlines and indentations). False, if the JSON needs to be in a single line.
     */
    public boolean getUsePrettyJson() {
        return usePrettyJson;
    }

    /**
     * Sets the {@link JsonFormatter}'s current "use pretty JSON" setting.
     * @param usePrettyJson True, if the serialized JSON needs to be in "pretty" format (with appropriate
     *                      newlines and indentations). False, if the JSON needs to be in a single line.
     */
    public void setUsePrettyJson(boolean usePrettyJson) {
        this.usePrettyJson = usePrettyJson;
    }

    /**
     * Gets the {@link Gson} object configured with the settings of this {@link JsonFormatter}.
     * @return The properly configured {@link Gson}.
     */
    public Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        if (getUsePrettyJson()) {
            gsonBuilder.setPrettyPrinting();
        }

        if (getUseExplicitExposeAnnotation()) {
            gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        }

        return gsonBuilder.create();
    }
}
