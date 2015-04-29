package hexaram.challengelyon.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by William on 29/04/2015.
 */
public class Challenge implements Serializable {

    protected String title;
    protected String summary;
    protected Date starttime;
    protected Date endtime;
    protected User creator;
    protected Category category;
    protected Type type;
    protected Quizz quizz;
    protected Metavalidation metavalidation;

    public Challenge(String title, String summary, User creator) {
        this.title = title;
        this.summary = summary;
        this.creator = creator;
    }
    public Challenge(String title, String summary, int reward) {
        this.title = title;
        this.summary = summary;
        this.category = new Category("", reward);
    }

    public Challenge(String title, String summary, User creator, Date starttime, Date endtime, Category category, Type type, Quizz quizz, Metavalidation metavalidation) {
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
        return category.getReward();
    }

    public String getCreator() {
        return creator.getUsername();
    }

    public Category getCategory() {
        return category;
    }

    public Type getType() {
        return type;
    }

    public String getSummary() {
        return summary;
    }


}
