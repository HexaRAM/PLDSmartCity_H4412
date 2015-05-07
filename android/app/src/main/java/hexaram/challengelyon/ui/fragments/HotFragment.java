package hexaram.challengelyon.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;
import hexaram.challengelyon.models.Metavalidation;
import hexaram.challengelyon.models.User;
import hexaram.challengelyon.services.requestAPI;
import hexaram.challengelyon.ui.activities.ChallengeViewActivity;
import hexaram.challengelyon.ui.adapter.ChallengeAdapter;


public class HotFragment extends Fragment {

    protected ArrayList<Challenge> challengeList;
    protected ChallengeAdapter<Challenge> adapter;
    protected ListView challengeListView;
    protected SwipeRefreshLayout refreshLayout;

    private final String CHALLENGE_PARAM = "challenge";

    public HotFragment() {
    }

    public static HotFragment newInstance(ArrayList<Challenge> list) {
        HotFragment challengeFragment = new HotFragment();
        Bundle args = new Bundle();
        args.putSerializable("Hot", list);
        challengeFragment.setArguments(args);
        return challengeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

//        if (getActivity() instanceof MainActivity) {
//            ((MainActivity) getActivity()).addOnListChangedListener(this);
//        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.challenge_list, container, false);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);


        /************ Refresh on scroll ***********/
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        /********** End Refresh on Scroll ********/

        challengeList = (ArrayList<Challenge>) getArguments().getSerializable("Hot");
        if(challengeList == null) {
            challengeList = new ArrayList<>();
        }
        getArguments().remove("Hot");
        //Initialiser l'adapter
            adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, challengeList);
            challengeListView = (ListView) rootView.findViewById(R.id.challenge_list);
        Log.d("list view", "" +challengeListView);
            //L'appliquer sur la listView
            challengeListView.setAdapter(adapter);


        challengeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               Challenge c = (Challenge) parent.getItemAtPosition(position);
               Intent intent = new Intent(getActivity(), ChallengeViewActivity.class);

               intent.putExtra(CHALLENGE_PARAM, c);

               startActivity(intent);

                /*FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment newFragment = ChallengeViewFragment.newInstance(c);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
                transaction.replace(R.id.drawer_layout, newFragment);
                transaction.addToBackStack(null);

// Commit the transaction
                transaction.commit();
*/

            }
        });
        return rootView;
    }

    void refreshItems()  {
        // Load items
        ArrayList<Challenge> newChallengeList = new ArrayList<Challenge>();

        try {

            /** HOT CHALLENGE LIST**/

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String token = prefs.getString("token","no_token");
            requestAPI req = new requestAPI(token);
            JSONObject response = req.getAllChallenges();
            JSONArray results = response.getJSONArray("results");
            Log.d("count", "" + response.getInt("count"));
            Log.d("url", results.getJSONObject(0).getString("url"));
            int count = response.getInt("count");
            for (int i = 0; i < count; i++) {
                JSONObject r = results.getJSONObject(i);
                String url = r.getString("url");
                String play = r.getString("play");
                String title = r.getString("title");
                String summary = r.getString("summary");
                String description = r.getString("description");
                String starttime = r.getString("starttime");
                String endtime = r.getString("endtime");
                int reward = r.getInt("reward");
                JSONObject user = r.getJSONObject("creator");
                User creator = new User(user.getString("url"), user.getString("email"), user.getInt("ranking"));
                Log.d("mail", user.getString("email") + " " + user.getString("ranking"));
                String category = r.getString("category");
                String type = r.getString("type");
                JSONObject metavalidation = r.getJSONObject("metavalidation");
                Metavalidation meta = new Metavalidation(metavalidation.getBoolean("picture_validation"), metavalidation.getBoolean("quizz_validation"), metavalidation.getBoolean("location_validation"));
                String quizz = r.getString("quizz");
                Challenge c = new Challenge(url, play, title, summary, description, starttime, endtime, creator, category, type, meta, quizz);
                c.setReward(reward);
                boolean played = r.getBoolean("played");
                if(played) {

                    c.setPlayed();
                }
                newChallengeList.add(c);
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
        // Load complete
        onItemsLoadComplete(newChallengeList);
    }
    void onItemsLoadComplete(ArrayList<Challenge> newChallenges) {
        // Update the adapter and notify data set changed
        onListChanged(newChallenges);

        // Stop refresh animation
        refreshLayout.setRefreshing(false);
    }


    @Override
    public void onSaveInstanceState(Bundle icicle){
        super.onSaveInstanceState(icicle);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (getActivity() instanceof MainActivity) {
//            ((MainActivity) getActivity()).removeOnListChangedListener(this);
//        }
    }

    public void onListChanged(List<Challenge> newList) {
        if (newList == null) {
            adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, challengeList);
        } else {
            adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, newList);
        }
        challengeListView.setAdapter(adapter);
    }

}
