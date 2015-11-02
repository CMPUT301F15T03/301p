package ca.ualberta.cmput301.t03.user;

import android.view.KeyEvent;
import android.widget.TextView;

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

    public CityInteractionListener getCityInteractionListener(){
        return new CityInteractionListener();
    }

    public EmailInteractionListener getEmailInteractionListener(){
        return new EmailInteractionListener();
    }

    public PhoneInteractionListener getPhoneInteractionListener(){
        return new PhoneInteractionListener();
    }

    public class CityInteractionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            setCity(v.getText().toString());
            return true;
        }
    }

    public class EmailInteractionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            setEmail(v.getText().toString());
            return true;
        }
    }


    public class PhoneInteractionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            setPhone(v.getText().toString());
            return true;
        }
    }

}