package hexaram.challengelyon.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hexaram.challengelyon.R;

public class AccessActivity extends ActionBarActivity {

    EditText textUserEmail;
    EditText textUserPassword;
    Button bLogin;
    Button bRegister;

    private CharSequence userEmail;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);
        textUserEmail = (EditText) findViewById(R.id.login_view_userEmail);
        userEmail = textUserEmail.getText();

        textUserPassword = (EditText) findViewById(R.id.login_view_userPassword);

        bLogin = (Button) findViewById(R.id.login_view_buttonLogin);
        bRegister  =(Button) findViewById(R.id.login_view_buttonInscription);

        bLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                //TODO
                //Use API to verify user
                boolean userIdentified = true; // Needs to be changed!!


                if (userIdentified){
                    startActivity(new Intent(AccessActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(AccessActivity.this,"Username and password not coorect",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_access, menu);
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
}
