package ca.ualberta.cmput301.t03;

import android.app.Application;
import android.content.Context;

/**
 * Created by rishi on 15-11-11.
 */
// Source: http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android
// November 11 2015
public class TradeApplication extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        TradeApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return TradeApplication.context;
    }
}
