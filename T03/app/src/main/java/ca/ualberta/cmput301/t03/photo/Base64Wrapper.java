package ca.ualberta.cmput301.t03.photo;

public class Base64Wrapper {
    private String contents;

    public Base64Wrapper() {
    }

    public Base64Wrapper(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
