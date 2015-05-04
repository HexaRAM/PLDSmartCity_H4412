package hexaram.challengelyon.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;

public class RealisationActivity extends ActionBarActivity {


    Button bUpload;
    Button bSubmit;
    Button bBack;
    ImageView photo;

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
        bUpload = (Button) findViewById(R.id.upload_photo);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
