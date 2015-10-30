package ca.ualberta.cmput301.t03.datamanager;

import com.google.gson.annotations.Expose;

/**
 * Created by rishi on 15-10-30.
 */
public class TestDto {
    @Expose
    private int aNumber;
    @Expose
    private String aString;
    @Expose
    private boolean aBoolean;
    private String aHiddenString;


    public TestDto(int aNumber, String aString, boolean aBoolean, String aHiddenString) {
        this.aNumber = aNumber;
        this.aString = aString;
        this.aBoolean = aBoolean;
        this.aHiddenString = aHiddenString;
    }

    public int getaNumber() {
        return aNumber;
    }

    public void setaNumber(int aNumber) {
        this.aNumber = aNumber;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public String getaHiddenString() {
        return aHiddenString;
    }

    public void setaHiddenString(String aHiddenString) {
        this.aHiddenString = aHiddenString;
    }
}
