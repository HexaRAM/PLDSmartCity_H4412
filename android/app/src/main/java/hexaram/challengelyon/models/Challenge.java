package hexaram.challengelyon.models;
import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by William on 29/04/2015.
 */
public class Challenge implements Serializable {

    protected String url;
    protected String description;
    protected String title;
    protected String summary;
    protected String play;
    protected String starttime;
    protected String endtime;
    protected User creator;
    protected String category;
    protected String type;
    protected String quizz;
    protected int reward;
    protected Metavalidation metavalidation;
    protected boolean played = false;

    public Challenge(String title, String summary, User creator) {
        this.title = title;
        this.summary = summary;
        this.creator = creator;
    }
    public Challenge(String title, String summary, String reward) {
        this.title = title;
        this.summary = summary;

    }

    public Challenge(String url, String play, String title, String description, String starttime, String endtime, User creator, String category, String type, Metavalidation metavalidation, String quizz) throws ParseException {
        this.title = title;
        this.summary = summary;
        this.play = play;
        this.creator = creator;
        this.starttime = starttime;
        this.endtime = endtime;
        this.category = category;
        this.type = type;
        this.quizz = quizz;
        this.metavalidation = metavalidation;

    }
    public Challenge(String title, String summary, User creator, String starttime, String endtime, String category, String type, String quizz, Metavalidation metavalidation) {
        this.title = title;
        this.summary = summary;
        this.creator = creator;
        this.starttime = starttime;
        this.endtime = endtime;
        this.category = category;
        this.type = type;
        this.quizz = quizz;
        this.metavalidation = metavalidation;

    }

    public Challenge(String url, String play, String title, String summary, String description, String starttime, String endtime, User creator, String category, String type, Metavalidation metavalidation, String quizz) {
        this.url = url;
        this.play = play;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.starttime = starttime;
        this.endtime = endtime;
        this.creator = creator;
        this.category = category;
        this.type = type;
        this.metavalidation = metavalidation;
        this.quizz = quizz;
    }

    public String getTitle() {
        return title;
    }

    public String getStarttime() {
        return starttime.toString();
    }

    public String getEndtime() {
        return endtime.toString();
    }


    public int getReward() {
        return reward;
    }

    public String getCreator() {
        return creator.getMail();
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getSummary() {
        return summary;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }


    public String getValidation() { return metavalidation.toString(); }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getPlay() {
        return play;
    }

    public boolean getPlayed(){return played;}

    public void setPlayed() {
        played = true;
    }
}
