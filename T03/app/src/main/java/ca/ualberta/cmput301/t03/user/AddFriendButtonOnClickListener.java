/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi
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

package ca.ualberta.cmput301.t03.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

public class AddFriendButtonOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        Snackbar.make(v, "Adding a friend", Snackbar.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setView(new EditText(v.getContext())); //todo replace with layout
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Add", null);
        builder.setTitle("Add a Friend");
        AlertDialog d = builder.create();
        d.show();
    }
}
