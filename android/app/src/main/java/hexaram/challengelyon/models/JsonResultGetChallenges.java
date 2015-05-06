package hexaram.challengelyon.models;

import android.util.Log;

import java.util.List;

/**
 * Created by maria on 05/05/15.
 */
public class JsonResultGetChallenges {
    protected int count;
    protected String next;
    protected String previous;
    protected List<Challenge> results;

    public int getCount(){
        Log.d("hey next", "" + previous.toString());
        return count;
    }
    public List<Challenge> getResults() {
        return results;
    }
}
