package hexaram.challengelyon.utils.JSonParser;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import hexaram.challengelyon.models.Challenge;

/**
 * Created by HP on 05/05/2015.
 */
public class JSonParser {

    public JSonParser() {
    }

    public ArrayList<Challenge> parseChallenges(InputStream source) {


        Gson gson = new Gson();

        Reader reader = new InputStreamReader(source);

        JsonChallengeAnswer response = gson.fromJson(reader, JsonChallengeAnswer.class);

        System.out.println("Il y a "+ response.getResults()+" challenge !");

        return response.getResults();

    }
}
