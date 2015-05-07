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
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import hexaram.challengelyon.R;
import hexaram.challengelyon.services.requestAPI;

public class ProfileViewActivity extends ActionBarActivity {

    ImageView imageItem;
    TextView textItemName;
    TextView textItemMail;
    TextView textItemAddress;
    TextView textItemNbPlayed;
    TextView textItemScore;
    TextView textItemRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_view);

        textItemName = (TextView) findViewById(R.id.profile_author_text);
        textItemScore = (TextView) findViewById(R.id.profile_score_text);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_view, menu);
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
                new AlertDialog.Builder(ProfileViewActivity.this)
                        .setTitle("Log out")
                        .setMessage("Do you want to log out?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProfileViewActivity.this);
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
                                Intent intent = new Intent(ProfileViewActivity.this, AccessActivity.class);
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
}
