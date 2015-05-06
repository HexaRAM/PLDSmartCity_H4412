package hexaram.challengelyon.models;

import java.io.Serializable;

/**
 * Created by maria on 29/04/15.
 */
public class Quizz implements Serializable {
    protected String name;
    protected String description;

    public Quizz(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}