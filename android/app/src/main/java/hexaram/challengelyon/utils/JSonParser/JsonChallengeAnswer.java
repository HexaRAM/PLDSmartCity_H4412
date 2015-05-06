package hexaram.challengelyon.utils.JSonParser;

import java.util.ArrayList;

import hexaram.challengelyon.models.Challenge;

/**
 * Created by HP on 05/05/2015.
 */
public class JsonChallengeAnswer {

    protected int count;
    protected String next;
    protected String previous;
    protected ArrayList<Challenge> results;

    public int getCount() {
        return count;
    }

    public ArrayList<Challenge> getResults() {
        return results;
    }

}
