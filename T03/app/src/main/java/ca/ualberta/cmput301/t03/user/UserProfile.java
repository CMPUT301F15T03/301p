package ca.ualberta.cmput301.t03.user;

import android.os.AsyncTask;

import com.google.gson.annotations.Expose;

import java.util.HashSet;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;

/**
 * Created by ross on 15-10-29.
 */
public class UserProfile implements Observable {
    public final static String type = "UserProfile";
    @Expose
    private String city;
    @Expose
    private String email;
    @Expose
    private String phone;

    private HashSet<Observer> observers;

    public UserProfile() {
        observers = new HashSet<>();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void commitChanges() {
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (Observer observer: observers) {
            observer.update(this);
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
