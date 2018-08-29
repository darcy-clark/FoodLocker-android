package org.foodlocker.structs;

public class User {

    private String username;
    private String passhash;
    private String type;

    public User(String username, String passhash) {
        this(username, passhash, null);
    }

    public User(String username, String passhash, String type) {
        this.username = username;
        this.passhash = passhash;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasshash() {
        return passhash;
    }

    public void setPasshash(String passhash) {
        this.passhash = passhash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
