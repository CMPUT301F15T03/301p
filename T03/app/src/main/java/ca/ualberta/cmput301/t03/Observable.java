package ca.ualberta.cmput301.t03;

/**
 * Created by ross on 15-10-29.
 */
public interface Observable {
    void notifyObservers();
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
}
