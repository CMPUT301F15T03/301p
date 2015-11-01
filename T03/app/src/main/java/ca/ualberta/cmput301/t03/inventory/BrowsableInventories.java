package ca.ualberta.cmput301.t03.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.ualberta.cmput301.t03.Filter;
import ca.ualberta.cmput301.t03.Filterable;
import ca.ualberta.cmput301.t03.user.FriendsList;
import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;

/**
 * Created by ross on 15-10-29.
 */
public class BrowsableInventories implements Filterable<Item>, Observer {
    private FriendsList friends;

    public ArrayList<Item> getBrowsables() {
        ArrayList<Item> list = new ArrayList<Item>();
        for(int i=0; i < 5; i++){
            list.add(new Item("test", "test"));
        }

        return list;
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
