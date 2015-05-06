package hexaram.challengelyon.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.ToValidate;
import hexaram.challengelyon.services.requestAPI;
import hexaram.challengelyon.utils.ImageLoader;

public class PictureValidationActivity extends ActionBarActivity implements View.OnClickListener {
    TextView titleChallengeValidation;
    TextView descriptionChallengeValidation;
    ImageView photoChallengeValidation;
    Button bValidateChallenge;
    Button bInvalidateChallenge;
    private final String CHALLENGE_PARAM_TITLE = "challenge_validation_title";
    private final String CHALLENGE_PARAM_DESCRIPTION = "challenge_validation_description";
    ToValidate tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_validation);

        Intent intent = getIntent();
        tv = (ToValidate)intent.getSerializableExtra("tovalidate");
        //TODO: get challenge ID from Intent <= ValidationFragment. => get title, description, photo with challenge_ID
        titleChallengeValidation = (TextView) findViewById(R.id.title_challenge_validation);
        descriptionChallengeValidation = (TextView) findViewById(R.id.description_challenge_validation);
        photoChallengeValidation = (ImageView) findViewById(R.id.photo_validation);

        bValidateChallenge = (Button) findViewById(R.id.button_validate_challenge);
        bInvalidateChallenge = (Button) findViewById(R.id.button_invalidate_challenge);

        titleChallengeValidation.setText(tv.getTitle());
        descriptionChallengeValidation.setText(tv.getDescription());
        bValidateChallenge.setOnClickListener(this);
        bInvalidateChallenge.setOnClickListener(this);

        /**DISPLAY PICTURE !**/
        // Loader image - will be shown before loading image
        int loader = R.drawable.ic_launcher_android;

        // Imageview to show
        ImageView image = (ImageView) findViewById(R.id.photo_validation);

        // Image url
        String image_url = tv.getPictures();

        // ImageLoader class instance
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());

        // whenever you want to load an image from url
        // call DisplayImage function
        // url - image url to load
        // loader - loader image, will be displayed before getting image
        // image - ImageView
        imgLoader.DisplayImage(image_url, loader, image);
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
        if (id == R.id.log_out) {
            new AlertDialog.Builder(PictureValidationActivity.this)
                    .setTitle("Log out")
                    .setMessage("Do you want to log out?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PictureValidationActivity.this);
                            String token = prefs.getString("token","no_token");
                            requestAPI req = new requestAPI(token);
                            try {
                                JSONObject responseLogout = req.logout();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(PictureValidationActivity.this, AccessActivity.class);
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

    @Override
    public void onClick(View v) {
        //Button validate Challenge
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PictureValidationActivity.this);
        String token = prefs.getString("token","no_token");
        requestAPI req = new requestAPI(token);
        if(v.getId()==R.id.button_validate_challenge)
        {
            try {
                req.clickURL(tv.getValidate());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }
        //Button invalidate Challenge
        if(v.getId()==R.id.button_invalidate_challenge)
        {
            try {
                req.clickURL(tv.getUnvalidate());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }
    }
}
