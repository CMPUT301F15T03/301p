package ca.ualberta.cmput301.t03.inventory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.user.FriendsListFragment;

/**
 * Created by rishi on 15-11-30.
 */
public class NoFriendsBrowseInventory extends Fragment implements Observer, SwipeRefreshLayout.OnRefreshListener {

    private View mView;
    private Inventory model;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Object uiLock = new Object();

    public NoFriendsBrowseInventory() {
        // Required empty public constructor
    }

    public static NoFriendsBrowseInventory newInstance() {
        return new NoFriendsBrowseInventory();
    }

    /**
     * Gets User associated to system config. Loads any data associated to Browsables.
     * Namely Users friendList.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        synchronized (uiLock) {
            mView = inflater.inflate(R.layout.no_friends_layout, container, false);
            final ImageView image =(ImageView) mView.findViewById(R.id.add_friends_icon);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddFriendsImagePressed(image);
                }
            });
            mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.noFriendsSwipeLayout);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        return mView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        if (model != null){
            model.removeObserver(this);
        }
        super.onDestroy();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.inventory_filter_menu_items, menu);
    }

    @Override
    public void update(Observable observable) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    PrimaryUser.getInstance().refresh();
                    model = PrimaryUser.getInstance().getBrowseableInventories(new ArrayList<FilterCriteria>());
                    //fixme browseableinventories actually needs to refresh
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                if (model.size() != 0) {
                    synchronized (uiLock) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContent, BrowseInventoryFragment.newInstance())
                                .commit();
                    }
                }
            }
        };
        task.execute();
    }

    private void onAddFriendsImagePressed(ImageView image) {
        Animation fadeout = new AlphaAnimation(1.f, 0.5f);
        fadeout.setDuration(100);
        image.startAnimation(fadeout);
        fadeout = new AlphaAnimation(0.5f, 1.f);
        fadeout.setDuration(100);
        image.startAnimation(fadeout);

        image.postDelayed(new Runnable() {
            @Override
            public void run() {
                addFriendsImageClicked();
            }
        }, 100);
    }

    private void addFriendsImageClicked() {
        synchronized (uiLock) {
            getActivity().setTitle(getString(R.string.friendsTitle));
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContent, FriendsListFragment.newInstance())
                    .commit();
        }
    }
}
