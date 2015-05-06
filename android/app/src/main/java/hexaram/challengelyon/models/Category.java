package hexaram.challengelyon.models;

import java.io.Serializable;

/**
 * Created by maria on 29/04/15.
 */
public class Category implements Serializable {
    protected String name;
    protected int reward;

    public Category(String name, int reward) {
        this.name = name;
        this.reward = reward;
    }

    public String getName() {
        return name;
    }

    public int getReward() {
        return reward;
    }
}