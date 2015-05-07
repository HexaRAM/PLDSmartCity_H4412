package hexaram.challengelyon.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;
import hexaram.challengelyon.services.requestAPI;
import hexaram.challengelyon.utils.MultipartUtility;


public class RealisationActivity extends ActionBarActivity  {


    FloatingActionButton bUpload;
    Button bSubmit;
    ImageView photo;
    Toolbar toolbar;


    Context context;

    private final String CHALLENGE_PARAM_ID = "challenge";
    private final int UPLOAD_ACTION = 945;
    private final int VALIDATE_ACTION = 946;
    String imageFile;

    TextView title;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realisation);

        Intent intent = getIntent();
        Challenge challenge = (Challenge)intent.getSerializableExtra("challenge");
        //String challengeID = intent.getStringExtra(CHALLENGE_PARAM_ID);

        context = getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.realisation_upload_view_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);

        photo = (ImageView) findViewById(R.id.uploaded_photo);
        bUpload = (FloatingActionButton) findViewById(R.id.upload_photo);
        bUpload.setColorNormalResId(R.color.colorPrimaryDark);
        bUpload.setColorPressedResId(R.color.colorPrimaryLight);
        bUpload.setIcon(R.drawable.ic_add_circle_red600_48dp);
        title = (TextView) findViewById(R.id.title_challenge);
        description = (TextView) findViewById(R.id.description_challenge);
        title.setText(challenge.getTitle());
        description.setText(challenge.getDescription());


        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent,945);


            }
        });

        bSubmit = (Button) findViewById(R.id.submit_challenge);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(RealisationActivity.this)
                        .setTitle("Submit challenge")
                        .setMessage("Are you sure you want to submit? After submit, no further changes can be done.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                 AsyncTask<String, Void, List<String>> task = new AsyncTask<String, Void, List<String>>() {
                                    @Override
                                    protected void onPostExecute(List<String> response) {
                                        super.onPostExecute(response);
                                        if(response == null)
                                        {
                                            Toast.makeText(RealisationActivity.this, getResources().getString(R.string.no_upload), Toast.LENGTH_SHORT).show();
                                        }
                                        else{

                                            Toast.makeText(RealisationActivity.this, getResources().getString(R.string.upload_ok), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    protected List<String> doInBackground(String... params) {
                                        try {
                                            MultipartUtility multipart = new MultipartUtility("http://vps165185.ovh.net/picturesChallengePlayed/", "UTF-8", "Token 9cd348ec7010d544cc74a44311ea22ff5b7dc02a");
                                            multipart.addFilePart("image", new File(imageFile));

                                            multipart.addFormField("description","petite description");
                                            multipart.addFormField("validationitem","2");



                                            List<String> response = multipart.finish();
                                            return response;
                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                            return null;
                                        }
                                    }
                                };
                                task.execute(imageFile);

                                setResult(RealisationActivity.RESULT_OK);
                                finish();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


        String token = "9cd348ec7010d544cc74a44311ea22ff5b7dc02a";

        /*try {
            //requestAPI req = new requestAPI(token);
            //JSONObject response = req.getAllChallenges();

        String token = "13e28143514cecdaac8387ce939052a0f6095bad";
        try {
            requestAPI req = new requestAPI(token);
            JSONObject response = req.getAllChallenges();
            //JSONObject responseUser = req.getUser("2");
            //JSONObject responseAllToValidate = req.getChallengesToValidate();
            //JSONArray responseAllPlayed = req.getAllChallengesPlayed();
            //JSONObject responsePlayChallenge = req.playChallenge("http://vps165185.ovh.net/challenges/3/play/");
            //JSONObject responseLogout = req.logout();


            //Log.d("user", responseUser.getString("email"));
            //Log.d("challenge", responsePlayChallenge.getString("status"));
           // Log.d("logout mess",responseLogout.getString("detail"));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //catch (JSONException e) {
         //   e.printStackTrace();
        //}*/

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

       //Action for upload photo click
        if(requestCode == UPLOAD_ACTION){
            if(data != null)
            {
                Uri selectedImageUri = data.getData();
                Log.d("MyTag", selectedImageUri+"");
                String filestring = getPath(selectedImageUri);
                imageFile = filestring;
                BitmapFactory.Options options2 = new BitmapFactory.Options();
                Bitmap thumbnail = BitmapFactory.decodeFile(filestring, options2);
                photo.setImageBitmap(thumbnail);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_realisation, menu);
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
            new AlertDialog.Builder(RealisationActivity.this)
                    .setTitle("Log out")
                    .setMessage("Do you want to log out?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RealisationActivity.this);
                            String token = prefs.getString("token","no_token");
                            requestAPI req = new requestAPI(token);
                            try {
                                JSONObject responseLogout = req.logout();
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("token", "logout");
                                editor.apply();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(RealisationActivity.this, AccessActivity.class);
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

    private String getPath(Uri uri) {
        String[]  data = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this.getApplicationContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
