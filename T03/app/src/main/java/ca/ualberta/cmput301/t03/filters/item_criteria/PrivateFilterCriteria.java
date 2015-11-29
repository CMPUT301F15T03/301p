package ca.ualberta.cmput301.t03.filters.item_criteria;

import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.inventory.Item;

/**
 * Created by quentinlautischer on 2015-11-23.
 */
public class PrivateFilterCriteria implements FilterCriteria {
    private String name = "private";
    private String type = "viewable";
    public String getName() {return this.name;}
    public String getType() {return this.type;}
    public boolean passes(Object o){
        return !((Item)o).isItemIsPrivate();
    }
}
