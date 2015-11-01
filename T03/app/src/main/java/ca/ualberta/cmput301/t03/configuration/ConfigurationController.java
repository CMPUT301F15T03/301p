/*
    Copyright (C) 2015 Kyle O'Shaughnessy
    Photography equipment trading application for CMPUT 301 at the University of Alberta.


    This file is part of {Application Name}.


    {Application Name} is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.ualberta.cmput301.t03.configuration;

/**
 * ConfigurationController is a controller for the Configuration model.
 * This class is responsible for providing an abstraction for event handlers setup by the view.
 */
public class ConfigurationController {
    private Configuration model;

    /**
     *
     * @param model The model which this controller will be modifying
     */
    public ConfigurationController(Configuration model) {
        this.model = model;
    }

    /**
     * Set the state of download images in the configuration
     * @param state The state desired for downloadImages in the configuration
     */
    public void onDownloadImagesToggled(Boolean state) {
        model.setDownloadImages(state);
    }
}
