package hexaram.challengelyon.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;
import hexaram.challengelyon.models.Metavalidation;
import hexaram.challengelyon.models.ToValidate;
import hexaram.challengelyon.models.User;
import hexaram.challengelyon.services.requestAPI;
import hexaram.challengelyon.ui.fragments.HotFragment;
import hexaram.challengelyon.ui.fragments.NavigationDrawerFragment;
import hexaram.challengelyon.ui.fragments.ValidationFragment;
import hexaram.challengelyon.ui.tabs.SlidingTabLayout;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener{

    private Toolbar toolbar;
    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;
    private SlidingTabLayout mSlide;
    ArrayList<Challenge> challengeList = new ArrayList<>();
    ArrayList<ToValidate> toValidateList = new ArrayList<>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.main_view_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /**
         * Get User info from server TO DO
         */
        user = new User("Hurle","imel",3);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, user);
/*
        Log.d("Tag", "OK");
        ProfileViewFragment profile = (ProfileViewFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        profile.setUp(user,(DrawerLayout) findViewById(R.id.drawer_layout));
        Log.d("Tag","OKKKKKK");*/

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        //Set up Tabbar
        mSlide = (SlidingTabLayout)findViewById(R.id.tabs);
        mSlide.setDistributeEvenly(true);
        mSlide.setViewPager(viewPager);


        try {
            /** HOT CHALLENGE LIST**/
            //TODO : get user TOKEN !
            String token = "da245e88375373c1b5bdf49f8a0b8f86fdeaecb9";
            requestAPI req = new requestAPI(token);
            JSONObject response = req.getAllChallenges();
            JSONArray results = response.getJSONArray("results");
            Log.d("count", ""+response.getInt("count"));
            Log.d("url", results.getJSONObject(0).getString("url"));
            int count = response.getInt("count");
            for (int i = 0; i<count; i++){
                JSONObject r = results.getJSONObject(i);
                String url = r.getString("url");
                String play = r.getString("play");
                String title = r.getString("title");
                String summary = r.getString("summary");
                String description = r.getString("description");
                String starttime = r.getString("starttime");
                String endtime = r.getString("endtime");
                JSONObject user = r.getJSONObject("creator");
                User creator = new User(user.getString("url"), user.getString("email"), user.getInt("ranking"));
                Log.d("mail", user.getString("email")+" "+user.getString("ranking"));
                String category = r.getString("category");
                String type = r.getString("type");
                JSONObject metavalidation = r.getJSONObject("metavalidation");
                Metavalidation meta = new Metavalidation(metavalidation.getBoolean("picture_validation"), metavalidation.getBoolean("quizz_validation"), metavalidation.getBoolean("location_validation"));
                String quizz = r.getString("quizz");
                Challenge c = new Challenge(url,play,title,summary,description,starttime,endtime,creator,category,type,meta,quizz);
                challengeList.add(c);
            }
            /** TO VALIDATE LIST**/
            response = req.getChallengesToValidate();
            results = response.getJSONArray("results");
            Log.d("count", ""+response.getInt("count"));
            count = response.getInt("count");
            for (int i = 0; i<count; i++){
                JSONObject r = results.getJSONObject(i);
                String validate = r.getString("validate");
                String unvalidate = r.getString("unvalidate");
                JSONObject challenge = r.getJSONObject("challenge");
                String url = challenge.getString("url");
                String title = challenge.getString("title");
                String summary = challenge.getString("summary");
                String description = challenge.getString("description");
                Boolean validated = r.getBoolean("validated");
                JSONArray pics = r.getJSONArray("pictures");
                String pictures ="";
                if(pics.length()!=0) {
                    pictures = r.getJSONArray("pictures").getString(0);
                }
                ToValidate tv = new ToValidate(validate, unvalidate, url, title, summary, description, validated, pictures);
                toValidateList.add(tv);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        if (id == R.id.log_out) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Log out")
                    .setMessage("Do you want to log out?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            //TODO Appel à l'API pour enrigestrer l'image

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
                case 1:
                    return ValidationFragment.newInstance(toValidateList);
            }
            return temp;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

}
