package ca.ualberta.cmput301.t03;

/**
 * Created by ross on 15-10-29.
 */
// TODO in the UML this interface has data members, which is not legal Java
public interface Filterable<T> {
    void addFilter(Filter filter);
    void removeFilter(Filter filter);
    void clearFilters();
    T getFilteredItems();
}
