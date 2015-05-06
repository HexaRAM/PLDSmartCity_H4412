package hexaram.challengelyon.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;
import hexaram.challengelyon.services.requestAPI;

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

    Toolbar toolbar;

    private static final int REALISATION_CHALLENGE = 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_view);
        Intent intent = getIntent();
        //Challenge challenge = (Challenge)intent.getSerializableExtra("challenge");
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.challenge_view_title);

        setSupportActionBar(toolbar);
        final Challenge challenge = (Challenge)intent.getSerializableExtra("challenge");
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
                //TODO : GET TOKEN
                String token = "9cd348ec7010d544cc74a44311ea22ff5b7dc02a";
                requestAPI req = new requestAPI(token);
                try {
                    req.clickURL(challenge.getPlay());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bTakeChallenge.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                Intent intent = new Intent(ChallengeViewActivity.this, RealisationActivity.class);
                intent.putExtra("challenge", challenge);
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

        textDescription.setText(challenge.getDescription());
        textCreatorName.setText(challenge.getCreator());
        textTitle.setText(challenge.getTitle());
        textStartTime.setText(challenge.getStarttime());
        textEndTime.setText(challenge.getEndtime());
        textCategory.setText(""+challenge.getCategory());
        textScore.setText(""+challenge.getReward());
        textValidation.setText(challenge.getValidation());
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
        if (id == R.id.log_out) {
            new AlertDialog.Builder(ChallengeViewActivity.this)
                    .setTitle("Log out")
                    .setMessage("Do you want to log out?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ChallengeViewActivity.this);
                            String token = prefs.getString("token","no_token");
                            requestAPI req = new requestAPI(token);
                            try {
                                JSONObject responseLogout = req.logout();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(ChallengeViewActivity.this, AccessActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
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
