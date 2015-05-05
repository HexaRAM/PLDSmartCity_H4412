package hexaram.challengelyon.models;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by maria on 29/04/15.
 */
public class Metavalidation implements Serializable {
    protected boolean picture_validation;
    protected boolean quizz_validation;
    protected boolean location_validation;

    public Metavalidation(boolean picture_validation, boolean quizz_validation, boolean location_validation) throws ParseException {
        this.picture_validation = picture_validation;
        this.quizz_validation = quizz_validation;
        this.location_validation = location_validation;
    }

    public boolean isPictureValidation() {
        return picture_validation;
    }
    public boolean isQuizzValidation() {
        return quizz_validation;
    }
    public boolean isLocationValidation() {
        return location_validation;
    }

    @Override
    public String toString() {
        String validationMode = "";
        if(picture_validation) {
            validationMode += "Photo ";
        }
        if(location_validation) {
            validationMode += "Localisation ";
        }
        if(quizz_validation) {
            validationMode += "Quizz ";
        }
        return validationMode;
    }
}