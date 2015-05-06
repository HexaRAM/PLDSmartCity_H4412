package hexaram.challengelyon.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;
import hexaram.challengelyon.models.ToValidate;
import hexaram.challengelyon.ui.activities.PictureValidationActivity;
import hexaram.challengelyon.ui.adapter.ChallengeAdapter;


public class ValidationFragment extends Fragment {

    protected ArrayList<Challenge> challengeList = new ArrayList<>();
    protected ArrayList<ToValidate> toValidateList = new ArrayList<>();
    protected ChallengeAdapter<ToValidate> adapter;
    protected ListView challengeListView;

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

    public void onListChanged(List<ToValidate> newList) {
        if (newList == null) {
            adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, toValidateList);
        } else {
            adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, newList);
        }
        challengeListView.setAdapter(adapter);
    }

}
