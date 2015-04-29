package hexaram.challengelyon.models;

/**
 * Created by William on 29/04/2015.
 */
public class Challenge {

    protected String title;
    protected String summary;
    protected int reward;

    public Challenge(String title, String summary, int reward) {
        this.title = title;
        this.summary = summary;
        this.reward = reward;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public int getReward() {
        return reward;
    }
}
