package hexaram.challengelyon.models;

import java.io.Serializable;

/**
 * Created by William on 06/05/2015.
 */
public class ToValidate implements Serializable{

    protected String validate;
    protected String unvalidate;
    protected String url;
    protected String title;
    protected String summary;
    protected String description;
    protected Boolean validated;
    protected String pictures;

    public ToValidate(String validate, String unvalidate, String url, String title, String summary, String description, Boolean validated, String pictures) {
        this.validate = validate;
        this.unvalidate = unvalidate;
        this.url = url;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.validated = validated;
        this.pictures = pictures;

    }

    public String getValidate() {
        return validate;
    }

    public String getUnvalidate() {
        return unvalidate;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Boolean getValidated() {
        return validated;
    }

    public String getDescription() {
        return description;
    }

    public String getPictures() {
        return pictures;
    }
}
