package ca.ualberta.cmput301.t03;

import java.util.HashSet;

/**
 * Created by ross on 15-10-29.
 */
public class UserProfile implements Observable {
    private String city;
    private String email;
    private String phone;
    private HashSet<Observer> observers;

    @Override
    public void notifyObservers() {
        for (Observer o: observers) {
            o.update(this);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
