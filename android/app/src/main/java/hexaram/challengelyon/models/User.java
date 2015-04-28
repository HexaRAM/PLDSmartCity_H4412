package hexaram.challengelyon.models;

import java.io.Serializable;

/**
 * Created by William on 28/04/2015.
 */
public class User implements Serializable {

    protected String username;
    protected String password;
    protected String mail;
    protected String address;

    public User(String address, String username, String mail) {
        this.address = address;
        this.username = username;
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
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
