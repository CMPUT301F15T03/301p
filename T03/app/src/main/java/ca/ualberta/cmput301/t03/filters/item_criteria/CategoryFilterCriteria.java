package ca.ualberta.cmput301.t03.filters.item_criteria;

import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.inventory.Item;

public class CategoryFilterCriteria implements FilterCriteria {
    private String category;
    private String name;
    private String type;
    public CategoryFilterCriteria(String category){
        this.category = category;
        this.name = category;
        this.type = "category";
    }
    public String getName() {return this.name;}
    public String getType() {return this.type;}
    public boolean passes(Object o){
        return ((Item)o).getItemCategory().toLowerCase().equals(category.toLowerCase());
    }
}
