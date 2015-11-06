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

package ca.ualberta.cmput301.t03.user;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;

import java.util.UnknownFormatConversionException;

/**
 * Controller for UserProfile.
 */
public class UserProfileController {
    private UserProfile mUserProfile;

    /**
     * Used to observe a text field and update the model.
     */
    TextWatcher cityWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setCity(s.toString());
        }
    };
    TextWatcher phoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setPhone(s.toString());
        }
    };
    TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setEmail(s.toString());
        }
    };

    UserProfileController(UserProfile userProfile) {
        mUserProfile = userProfile;
    }

    public void setName(String name) {
        throw new UnknownFormatConversionException("you can't do that!");
    }

    public void setPhone(String phone) {
        mUserProfile.setPhone(phone);
    }

    public void setCity(String city) {
        mUserProfile.setCity(city);
    }

    public void setEmail(String email) {
        if (!isEmailInValid(email)) {
            mUserProfile.setEmail(email);
        }
    }

    public void commitChanges() {
        mUserProfile.commitChanges();
    }


    /**
     * Check to see if an email matches the valid syntax.
     *
     * @param email email that will be validated
     * @return true == valid, false == invalid
     */
    public boolean isEmailInValid(String email) {
        return email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public TextWatcher getCityWatcher() {
        return cityWatcher;
    }

    public TextWatcher getPhoneWatcher() {
        return phoneWatcher;
    }

    public TextWatcher getEmailWatcher() {
        return emailWatcher;
    }

}