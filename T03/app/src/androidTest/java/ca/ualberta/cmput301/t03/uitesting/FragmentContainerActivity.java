package ca.ualberta.cmput301.t03.uitesting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by quentinlautischer on 2015-11-03.
 */
public class FragmentContainerActivity extends FragmentActivity {

    private static final int CONTAINER_ID = View.generateViewId();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(CONTAINER_ID);

        setContentView(frameLayout, params);
    }

    public void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .add(CONTAINER_ID, fragment, tag)
                .commit();
    }
}