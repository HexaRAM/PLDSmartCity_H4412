package hexaram.challengelyon.models;

import java.io.Serializable;

/**
 * Created by maria on 29/04/15.
 */
public class Type implements Serializable {
    protected String name;

    public Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}