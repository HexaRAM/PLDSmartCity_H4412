package hexaram.challengelyon.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import hexaram.challengelyon.R;

public class AccessActivity extends ActionBarActivity {

    EditText textUserEmail;
    EditText textUserPassword;
    Button bLogin;
    Button bRegister;

    private String userEmail;
    private String userPassword;
    private String auth_token;

    private Context context;
    private final String API_HOST = "http://vps165185.ovh.net/auth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);
        textUserEmail = (EditText) findViewById(R.id.login_view_userEmail);
        textUserPassword = (EditText) findViewById(R.id.login_view_userPassword);

        bLogin = (Button) findViewById(R.id.login_view_buttonLogin);
        bRegister = (Button) findViewById(R.id.login_view_buttonInscription);



        bLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userEmail = textUserEmail.getText().toString();
                userPassword = textUserPassword.getText().toString();
                /*
                // Perform action on click

                Ion.with(getApplicationContext())
                        .load(API_HOST+"/login")
                        .setLogging("MyLogs", Log.DEBUG)
                        .setBodyParameter("email", userEmail)
                        .setBodyParameter("password", userPassword)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                auth_token = result;
                                //Log.d("auth_token", auth_token);
                                if (auth_token != null){

                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.putExtra("auth_token",result);
                                    startActivity(i);
                                    startActivity(new Intent(AccessActivity.this, MainActivity.class));
                                }
                                else
                                    Toast.makeText(AccessActivity.this,"Username and password not correct",Toast.LENGTH_SHORT).show();
                            }
                        });



            }

        });*/

                AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (TextUtils.isEmpty(s)) {
                            Toast.makeText(AccessActivity.this, getResources().getString(R.string.no_login), Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println("TOKEN : " + s);
                            Toast.makeText(AccessActivity.this, getResources().getString(R.string.login_ok), Toast.LENGTH_SHORT).show();
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AccessActivity.this);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("token", s);
                            editor.apply();
                            startActivity(new Intent(AccessActivity.this, MainActivity.class));

                        }
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        HttpURLConnection urlConnection;

                        try {
                            URL login_url = new URL("http://vps165185.ovh.net/auth/login");
                            urlConnection = (HttpURLConnection) login_url.openConnection();
                            urlConnection.setRequestMethod("POST");
                            urlConnection.setDoOutput(true);

                            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

                            os.writeBytes(("email=" + URLEncoder.encode(userEmail, "UTF-8") + "&password=" + URLEncoder.encode(userPassword, "UTF-8")));
                            os.flush();
                            os.close();

                            int code = urlConnection.getResponseCode();
                            System.out.println("CODE : " + code);
                            if (code != 200) {
                                return null;
                            } else {
                                byte[] buffer = new byte[128];
                                InputStream is = urlConnection.getInputStream();
                                is.read(buffer);
                                is.close();
                                String token = new String(buffer);
                                token = token.substring(token.indexOf(":") + 2, token.lastIndexOf("\""));
                                System.out.println("TOKEN : " + token);
                                urlConnection.disconnect();
                                return token;
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                task.execute();


            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AccessActivity.this);
        if(prefs.contains("token")) {
            String token = prefs.getString("token","no_token");
            if(token.equals("logout") == false) {
                //startActivity(new Intent(AccessActivity.this, MainActivity.class));
                Log.d("connected","redirect");
            }

        }
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
