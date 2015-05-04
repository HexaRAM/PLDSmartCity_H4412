package hexaram.challengelyon.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;

public class ChallengeViewActivity extends ActionBarActivity {

    TextView textCreatorName;
    TextView textDescription;
    TextView textTitle;
    TextView textStartTime;
    TextView textEndTime;
    TextView textCategory;
    TextView textValidation;
    TextView textScore;
    Button bTakeChallenge;
    Button bBack;

    private static final int REALISATION_CHALLENGE = 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_view);

        textCreatorName = (TextView) findViewById(R.id.challenge_view_creator);
        textDescription = (TextView) findViewById(R.id.challenge_view_description);
        textTitle = (TextView) findViewById(R.id.challenge_view_title);
        textStartTime = (TextView) findViewById(R.id.challenge_view_starttime);
        textEndTime = (TextView) findViewById(R.id.challenge_view_endtime);
        textCategory = (TextView) findViewById(R.id.challenge_view_category);
        textValidation = (TextView) findViewById(R.id.challenge_view_validation);
        textScore = (TextView) findViewById(R.id.challenge_view_reward);

        bTakeChallenge = (Button) findViewById(R.id.take_challenge);
        bTakeChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bTakeChallenge.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                Intent intent = new Intent(ChallengeViewActivity.this, RealisationActivity.class);
                startActivityForResult(intent, REALISATION_CHALLENGE);
            }
        });

        bBack = (Button) findViewById(R.id.back_button);
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RealisationActivity.RESULT_OK);
                finish();
            }
        });

        Challenge challenge = new Challenge("title","summary",1);
        /*textDescription.setText(challenge.getSummary());
        textCreatorName.setText(challenge.getCreator());
        textTitle.setText(challenge.getTitle());
        textStartTime.setText(challenge.getStarttime());
        textEndTime.setText(challenge.getEndtime());
        textCategory.setText(challenge.getCategory().getName());
        textScore.setText(challenge.getReward());
        textValidation.setText(challenge.getValidation());*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_challenge_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Action for returning from RealisationChallenge
        if(requestCode == REALISATION_CHALLENGE){
            bTakeChallenge.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}
