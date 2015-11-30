package ca.ualberta.cmput301.t03.filters.item_criteria;

import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.inventory.Item;

/**
 * Created by quentinlautischer on 2015-11-23.
 */
public class StringQueryFilterCriteria implements FilterCriteria {
    private String name;
    private String term;
    private String type;
    public StringQueryFilterCriteria(){}
    public StringQueryFilterCriteria(String term){
        this.term = term;
        this.name = term;
        this.type = "textual";
    }
    public String getName() {return this.name;}
    public String getType() {return this.type;}
    public boolean passes(Object o){
        if ( ((Item)o).getItemName().toLowerCase().contains(term.toLowerCase()) ){
            return true;
        }
        if ( ((Item)o).getItemDescription().toLowerCase().contains(term.toLowerCase()) ){
            return true;
        }
        return false;
    }

}
