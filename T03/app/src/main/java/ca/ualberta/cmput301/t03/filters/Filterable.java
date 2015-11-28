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

package ca.ualberta.cmput301.t03.filters;

// TODO in the UML this interface has data members, which is not legal Java


import java.util.ArrayList;
import java.util.List;

/**
 * Filterable allows a collection of some data to have filters applied to it that will change
 * what gets returned by getFilteredItems. This is useful for views that dont want to modify data,
 * but just relieve filtered data.
 *
 * @param <T>
 */
public interface Filterable<T> {
    /**
     * Add a filter to the data, this may or may not modify what is returned by getFilteredItems()
     *
     * @param filter the filter you wish to apply
     */
    void addFilter(FilterCriteria filter);

    /**
     * Remove a filter from the data, this may or may not modify what is returned by
     * getFilteredItems().
     *
     * @param filter the filter you wish to remove
     */
    void removeFilter(String filterName);

    /**
     * Remove all filters from the data, this may or may not modify what is returned by
     * getFilteredItems().
     */
    void clearFilters();

    /**
     * Get the filtered T back from the model, this will be a subset of the non-filtered data.
     *
     * @return
     */
    List<T> getFilteredItems(ArrayList<T> list, List<FilterCriteria> filters);
}
