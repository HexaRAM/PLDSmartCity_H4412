package hexaram.challengelyon.models;

import java.io.Serializable;

/**
 * Created by William on 28/04/2015.
 */
public class User implements Serializable {

    protected String username;
    protected String password;
    protected String email;
    protected String address;
    protected int ranking;
    protected String url;

    public User(String address, String username, String mail) {
        this.address = address;
        this.username = username;
        this.email = mail;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return email;
    }

    public int getNbPlayed(){
        // TO DO
        return 30;
    }

    public int getScore(){
        // TO DO
        return 300;
    }

    public int getRank(){
        //TO DO
        return 1;
    }
}
