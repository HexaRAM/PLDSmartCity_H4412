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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;
import hexaram.challengelyon.models.ToValidate;
import hexaram.challengelyon.services.requestAPI;
import hexaram.challengelyon.ui.activities.PictureValidationActivity;
import hexaram.challengelyon.ui.adapter.ChallengeAdapter;


public class ValidationFragment extends Fragment {

    protected ArrayList<Challenge> challengeList = new ArrayList<>();
    protected ArrayList<ToValidate> toValidateList = new ArrayList<>();
    protected ChallengeAdapter<ToValidate> adapter;
    protected ListView challengeListView;
    protected SwipeRefreshLayout refreshLayout;

    private final String TOVALIDATE_PARAM = "tovalidate";

    public ValidationFragment() {
    }

    public static ValidationFragment newInstance(ArrayList<ToValidate> list) {
        ValidationFragment challengeFragment = new ValidationFragment();
        Bundle args = new Bundle();
        args.putSerializable("Validation", list);
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

//        challengeList = (ArrayList<Challenge>) getArguments().getSerializable("Validation");
        toValidateList = (ArrayList<ToValidate>) getArguments().getSerializable("Validation");
        getArguments().remove("Validation");
//        for(ToValidate tv : toValidateList){
//            Challenge c = new Challenge(tv.getTitle(), tv.getSummary(), -1);
//            challengeList.add(c);
//        }
        //Initialiser l'adapter
        adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, toValidateList);
        challengeListView = (ListView) rootView.findViewById(R.id.challenge_list);
        //L'appliquer sur la listView
        challengeListView.setAdapter(adapter);


        challengeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ToValidate tv = (ToValidate) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), PictureValidationActivity.class);
                //TODO: putExtra ID not TITLE/DESCRIPTION
                intent.putExtra(TOVALIDATE_PARAM, tv);
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
    void refreshItems()  {
        // Load items
        ArrayList<ToValidate> newChallengeList = new ArrayList<ToValidate>();

        try {

            /** HOT CHALLENGE LIST**/

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String token = prefs.getString("token","no_token");
            requestAPI req = new requestAPI(token);
            JSONObject response = req.getChallengesToValidate();
            JSONArray results = response.getJSONArray("results");
            Log.d("count", ""+response.getInt("count"));
            int count = response.getInt("count");
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
                int reward = req.clickURL(url).getInt("reward");
                tv.setReward(reward);


                newChallengeList.add(tv);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Load complete
        onItemsLoadComplete(newChallengeList);
    }
    void onItemsLoadComplete(ArrayList<ToValidate> newChallenges) {
        // Update the adapter and notify data set changed
        onListChanged(newChallenges);

        // Stop refresh animation
        refreshLayout.setRefreshing(false);
    }


    public void onListChanged(List<ToValidate> newList) {
        if (newList == null) {
            adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, toValidateList);
        } else {
            adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, newList);
        }
        challengeListView.setAdapter(adapter);
    }

}
