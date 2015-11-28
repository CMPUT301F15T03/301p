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

package ca.ualberta.cmput301.t03.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.inventory.Item;

public class TileBuilder {
    private Resources resources;

    public TileBuilder(Resources resources) {
        this.resources = resources;
    }

    public ArrayList<HashMap<String, Object>> buildItemTiles(Collection<Item> items, HashMap<Integer, UUID> positionMap) {
        ArrayList<HashMap<String, Object>> tiles = new ArrayList<>();

        int i = 0;
        positionMap.clear();
        for (Item item : items) {
            HashMap<String, Object> hm = new HashMap<String, Object>();
            hm.put("tileViewItemName", item.getItemName());
            hm.put("tileViewItemCategory", item.getItemCategory());
            if (item.getPhotoList().getPhotos().size() > 0) {
                hm.put("tileViewItemImage", (Bitmap) item.getPhotoList().getPhotos().get(0).getPhoto());
            } else {
                hm.put("tileViewItemImage", ((BitmapDrawable) resources.getDrawable(R.drawable.photo_unavailable)).getBitmap());
            }
            tiles.add(hm);
            positionMap.put(i, item.getUuid());
            i++;
        }

        return tiles;
    }
}
