package ca.ualberta.cmput301.t03.user;

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
        mUserProfile.commitChanges();
    }

    public void setCity(String city) {
        mUserProfile.setCity(city);
        mUserProfile.commitChanges();
    }

    public void setEmail(String email) {
        mUserProfile.setEmail(email);
        mUserProfile.commitChanges();
    }
}
