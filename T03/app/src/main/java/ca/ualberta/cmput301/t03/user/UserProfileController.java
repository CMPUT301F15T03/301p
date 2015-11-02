package ca.ualberta.cmput301.t03.user;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.UnknownFormatConversionException;

/**
 * Created by ross on 15-10-29.
 */
public class UserProfileController {
    private UserProfile mUserProfile;

    UserProfileController(UserProfile userProfile){
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
        mUserProfile.setEmail(email);
    }

    public void commitChanges(){
        mUserProfile.commitChanges();
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

}