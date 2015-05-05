package hexaram.challengelyon.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import hexaram.challengelyon.R;

public class PictureValidationActivity extends ActionBarActivity implements View.OnClickListener {
    TextView titleChallengeValidation;
    TextView descriptionChallengeValidation;
    ImageView photoChallengeValidation;
    Button bValidateChallenge;
    Button bInvalidateChallenge;
    private final String CHALLENGE_PARAM_TITLE = "challenge_validation_title";
    private final String CHALLENGE_PARAM_DESCRIPTION = "challenge_validation_description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_validation);

        Intent intent = getIntent();
        //TODO: get challenge(played) ID
        String challengeTitle = intent.getStringExtra(CHALLENGE_PARAM_TITLE);
        String challengeDescription = intent.getStringExtra(CHALLENGE_PARAM_DESCRIPTION);
        //TODO: get challenge ID from Intent <= ValidationFragment. => get title, description, photo with challenge_ID
        titleChallengeValidation = (TextView) findViewById(R.id.title_challenge_validation);
        descriptionChallengeValidation = (TextView) findViewById(R.id.description_challenge_validation);
        photoChallengeValidation = (ImageView) findViewById(R.id.photo_validation);

        bValidateChallenge = (Button) findViewById(R.id.button_validate_challenge);
        bInvalidateChallenge = (Button) findViewById(R.id.button_invalidate_challenge);

        titleChallengeValidation.setText(challengeTitle);
        descriptionChallengeValidation.setText(challengeDescription);
        bValidateChallenge.setOnClickListener(this);
        bInvalidateChallenge.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture_validation, menu);
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

    @Override
    public void onClick(View v) {
        //Button validate Challenge
        if(v.getId()==R.id.button_validate_challenge)
        {
            //TODO: web service validate challenge
            Intent intent = new Intent(PictureValidationActivity.this, MainActivity.class);
            startActivity(intent);
        }
        //Bitton invalidate Challenge
        if(v.getId()==R.id.button_invalidate_challenge)
        {
            //TODO: web service invalidate challenge
            Intent intent = new Intent(PictureValidationActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
