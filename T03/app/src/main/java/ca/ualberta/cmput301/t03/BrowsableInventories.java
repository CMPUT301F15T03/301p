package ca.ualberta.cmput301.t03;

import java.util.Collection;

/**
 * Created by ross on 15-10-29.
 */
public class BrowsableInventories implements Filterable<Item>, Observer {
    private FriendsList friends;

    public Collection<Item> getBrowsables() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addFilter(Filter filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeFilter(Filter filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearFilters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item getFilteredItems() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Observable observable) {
        throw new UnsupportedOperationException();
    }
}
