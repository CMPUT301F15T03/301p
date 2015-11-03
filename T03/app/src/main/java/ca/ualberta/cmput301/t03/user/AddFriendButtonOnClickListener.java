package ca.ualberta.cmput301.t03.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

/**
 * Copyright 2015 John Slevinsky
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
