package hexaram.challengelyon.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import hexaram.challengelyon.R;
import hexaram.challengelyon.ui.fragments.MyMapFragment;

public class LaMapActivity extends ActionBarActivity implements MyMapFragment.OnFragmentInteractionListener{

    MyMapFragment laMap;
    Button bSubmitDestination;
    Button bSubmitChallenge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_la_map);

        bSubmitDestination = (Button) findViewById(R.id.bSubmitDest);
        bSubmitDestination.setVisibility(View.GONE);
        bSubmitChallenge = (Button) findViewById(R.id.bSubmitChallenge);
        bSubmitChallenge.setVisibility(View.GONE);
        laMap = MyMapFragment.newInstance();
        laMap.setContext(this);
        laMap.setButton(bSubmitDestination,bSubmitChallenge);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container,laMap ).commit();
        new AlertDialog.Builder(LaMapActivity.this)
                .setTitle("Submit challenge")
                .setMessage("Selectionnez la destination sur la carte")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_info)
                .show();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_la_map, menu);
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
    public void onFragmentInteraction(Uri uri) {

    }


}
