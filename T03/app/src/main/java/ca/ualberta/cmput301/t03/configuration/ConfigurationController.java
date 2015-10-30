/*
    Copyright (C) 2015 "CMPUT301F15T03", Kyle O'Shaughnessy
    Photography equipment trading application for CMPUT 301 at the University of Alberta


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

public class ConfigurationController {
    private Configuration model;

    public ConfigurationController(Configuration model) {
        this.model = model;
    }

    public void onOfflineModeToggled(Boolean switchState) {
        model.setOfflineModeEnabled(switchState);
    }

    public void onDownloadImagesToggled(Boolean switchState) {
        model.setDownloadImagesEnabled(switchState);
    }
}
