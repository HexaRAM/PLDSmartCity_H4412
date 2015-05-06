package hexaram.challengelyon.ui.activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.User;
import hexaram.challengelyon.services.requestAPI;

public class RealisationActivity extends ActionBarActivity {


    FloatingActionButton bUpload;
    Button bSubmit;
    Button bBack;
    ImageView photo;
    Toolbar toolbar;

    Context context;

    private final String CHALLENGE_PARAM_ID = "challenge";
    private final int UPLOAD_ACTION = 945;
    private final int VALIDATE_ACTION = 946;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realisation);

        Intent intent = getIntent();
        String challengeID = intent.getStringExtra(CHALLENGE_PARAM_ID);

        context = getApplicationContext();

        photo = (ImageView) findViewById(R.id.uploaded_photo);
        bUpload = (FloatingActionButton) findViewById(R.id.upload_photo);
        bUpload.setColorNormalResId(R.color.colorPrimaryDark);
        bUpload.setColorPressedResId(R.color.colorPrimaryLight);
        bUpload.setIcon(R.drawable.ic_add_circle_red600_48dp);



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

                                //TODO Appel à l'API pour enrigestrer l'image

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

        bBack = (Button) findViewById(R.id.back_button);
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.realisation_upload_view_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try {
            requestAPI req = new requestAPI();
            JSONObject response = req.getMyJSONObjet();
            Log.d("email", response.getString("email"));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
     /*   int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);*/
        onBackPressed();
        return true;
    }

    private String getPath(Uri uri) {
        String[]  data = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this.getApplicationContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    /*public class PostFetcher extends AsyncTask<Void, Void, String> {
        private static final String TAG = "Heeeyyyyyyy";
        public static final String SERVER_URL = "http://vps165185.ovh.net/users/1";

        @Override
        protected String doInBackground(Void... params) {
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(SERVER_URL);
                get.addHeader("Authorization", "Token 1a7d6b30a23da000c84d287f8f7fd0152412a9f9");

                //Perform the request and check the status code
                HttpResponse response = client.execute(get);
                StatusLine statusLine = response.getStatusLine();


                /*ResponseHandler<String> responseHandler=new BasicResponseHandler();
                String responseBody = client.execute(get, responseHandler);
                JSONObject responseS=new JSONObject(responseBody);
                JSONArray resultList =  responseS.getJSONArray("results");
                String play = resultList.getJSONObject(0).getString("play");
                Log.d("Maria",play);
                String fluxJson ="";*/

               /* if (statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    //InputStream content = entity.getContent();
                    if(entity!=null) {
                        fluxJson = EntityUtils.toString(entity, HTTP.UTF_8);
                        fluxJson = "{" + "\"userR\"" + ": " + fluxJson + "}";
                        Log.d("Tuuuuup",fluxJson);
                    }

                    try {
                        //Read the server response and attempt to parse it as JSON





                        //BufferedReader reader =  new BufferedReader(new InputStreamReader(content));
                        Gson gson = new Gson();

                        UserR u = new UserR();
                        u  = (UserR) gson.fromJson(fluxJson, UserR.class);
                        Log.d("maria ", ""+ u.url);



                        //content.close();

                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to parse JSON due to: " + ex);

                    }
                } else {
                    Log.e(TAG, "Server responded with status code: " + statusLine.getStatusCode());

                }
            } catch (Exception ex) {
                Log.e(TAG, "Failed to send request due to: " + ex);

            }
            return null;
        }
    }
    public class UserGet extends AsyncTask<Void, Void, String> {
        private static final String TAG = "USer";
        public static final String SERVER_URL = "http://vps165185.ovh.net/users/1/";

        @Override
        protected String doInBackground(Void... params) {
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(SERVER_URL);
                get.addHeader("Authorization", "Token 1a7d6b30a23da000c84d287f8f7fd0152412a9f9");

                //Perform the request and check the status code
                ResponseHandler<String> responseHandler=new BasicResponseHandler();
                String responseBody = client.execute(get, responseHandler);
                JSONObject responseS=new JSONObject(responseBody);


                Gson gson = new Gson();
                User resp = gson.fromJson(String.valueOf(responseS), User.class);
                Log.d("maria user", ""+ resp.getUsername());
                //User u = new User(responseS.getString("url"), responseS.getString("email"), responseS.getInt("ranking"));
                //Log.d("json", u.getUsername());
                Log.d("testtt", responseS.getString("email"));


                /*HttpResponse response = client.execute(get);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    try {
                        //Read the server response and attempt to parse it as JSON
                        BufferedReader reader =  new BufferedReader(new InputStreamReader(content));
                        Gson gson = new Gson();
                        User resp = gson.fromJson(reader, User.class);
                        Log.d("maria user", ""+ resp.getUsername());





                        content.close();

                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to parse JSON due to: " + ex);

                    }
                } else {
                    Log.e(TAG, "Server responded with status code: " + statusLine.getStatusCode());

                }*/

}
