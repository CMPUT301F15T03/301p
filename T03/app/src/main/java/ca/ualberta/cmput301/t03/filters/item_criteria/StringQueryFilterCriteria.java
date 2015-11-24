package ca.ualberta.cmput301.t03.filters.item_criteria;

import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.inventory.Item;

/**
 * Created by quentinlautischer on 2015-11-23.
 */
public class StringQueryFilterCriteria implements FilterCriteria {
    private String name;
    public boolean passes(Object o){
        return ((Item)o).getItemName().equals(name);
    }
}
