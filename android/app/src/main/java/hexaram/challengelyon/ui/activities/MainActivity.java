package hexaram.challengelyon.ui.activities;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.support.v4.app.FragmentTransaction;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.google.gson.JsonObject;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ResponseFuture;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;
import hexaram.challengelyon.models.User;
import hexaram.challengelyon.ui.fragments.HotFragment;
import hexaram.challengelyon.ui.fragments.NavigationDrawerFragment;
import hexaram.challengelyon.ui.fragments.ProfileViewFragment;
import hexaram.challengelyon.ui.tabs.SlidingTabLayout;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener{

    private Toolbar toolbar;
    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;
    private SlidingTabLayout mSlide;
    ArrayList<Challenge> challengeList = new ArrayList<>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new
        StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /**
         * Get User info from server TO DO
         */
        user = new User("Villeurbanne","hexaram","hexaram@insa-lyon.fr");

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, user);
/*
        Log.d("Tag", "OK");
        ProfileViewFragment profile = (ProfileViewFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        profile.setUp(user,(DrawerLayout) findViewById(R.id.drawer_layout));
        Log.d("Tag","OKKKKKK");*/
        /**
        * Get Challenge List from server TO DO !
         */
        Challenge c1 = new Challenge("Challenge1", "Le premier challenge", 100);
        Challenge c2 = new Challenge("Challenge2", "Le deuxième challenge", 200);
        Challenge c3 = new Challenge("Challenge3", "Le troisième challenge c'est la foliiiiie", 1000);
        challengeList.add(c1);
        challengeList.add(c2);
        challengeList.add(c3);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        //Set up Tabbar
        mSlide = (SlidingTabLayout)findViewById(R.id.tabs);
        mSlide.setViewPager(viewPager);
        makePostRequest();


        //testing the ION library

       Ion.with(getApplicationContext())
                .load("http://vps165185.ovh.net/auth/login")
               .setLogging("MyLogs", Log.DEBUG)
                .setBodyParameter("email", "salma@hexaram.com")
                .setBodyParameter("password", "test")
                .asString()
               .setCallback(new FutureCallback<String>() {
                   @Override
                   public void onCompleted(Exception e, String result) {
                       Log.d("Salma", result+" OK");
                   }
               });






    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Toast.makeText(this, R.string.toast_params, Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }



    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] tabs;
        public SectionsPagerAdapter(FragmentManager fm) {

            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment temp = new Fragment();
           // temp = HotFragment.newInstance(challengeList);

            switch(position) {
                case 0:
                    return HotFragment.newInstance(challengeList);
            }
            return temp;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    private void makePostRequest() {


        HttpClient httpClient = new DefaultHttpClient();
        // replace with your url
        HttpPost httpPost = new HttpPost("http://vps165185.ovh.net/auth/login");


        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("email", "cosmi@hexaram.com"));
        nameValuePair.add(new BasicNameValuePair("password", "cosmi"));


        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);
            // write response to log
            Reader in = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder= new StringBuilder();
            char[] buf = new char[1000];
            int l = 0;
            while (l >= 0) {
                builder.append(buf, 0, l);
                l = in.read(buf);
            }
            JSONTokener tokener = new JSONTokener( builder.toString() );
            Log.d("Http Post Response:", builder.toString());
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }

    }

}

