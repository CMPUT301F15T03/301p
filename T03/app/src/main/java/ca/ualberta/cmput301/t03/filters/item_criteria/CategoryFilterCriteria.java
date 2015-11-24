package ca.ualberta.cmput301.t03.filters.item_criteria;

import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.inventory.Item;

public class CategoryFilterCriteria implements FilterCriteria {
    private String category;
    public boolean passes(Object o){
        return ((Item)o).getItemCategory().equals(category);
    }
}
