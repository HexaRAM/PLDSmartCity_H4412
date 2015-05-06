package hexaram.challengelyon.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

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
    protected int category;
    protected int type;
    protected String quizz;
    protected Metavalidation metavalidation;

    public Challenge(String title, String summary, User creator) {
        this.title = title;
        this.summary = summary;
        this.creator = creator;
    }
    public Challenge(String title, String summary, int reward) {
        this.title = title;
        this.summary = summary;
        this.category = reward;
    }

    public Challenge(String url, String play, String title, String description, String starttime, String endtime, User creator, int category, int type, Metavalidation metavalidation, String quizz) throws ParseException {
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
    public Challenge(String title, String summary, User creator, String starttime, String endtime, int category, int type, String quizz, Metavalidation metavalidation) {
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

    public Challenge(String url, String play, String title, String summary, String description, String starttime, String endtime, User creator, int category, int type, Metavalidation metavalidation, String quizz) {
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
        return starttime.toString();
    }

    public int getReward() {
        return category;
    }

    public String getCreator() {
        return creator.getMail();
    }

    public int getCategory() {
        return category;
    }

    public int getType() {
        return type;
    }

    public String getSummary() {
        return summary;
    }

    public String getValidation() { return metavalidation.toString(); }

    public String getDescription() {
        return description;
    }
}
