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
import hexaram.challengelyon.ui.activities.PictureValidationActivity;
import hexaram.challengelyon.ui.adapter.ChallengeAdapter;


public class ValidationFragment extends Fragment {

    protected ArrayList<Challenge> challengeList;
    protected ChallengeAdapter<Challenge> adapter;
    protected ListView challengeListView;

    private final String CHALLENGE_PARAM_TITLE = "challenge_validation_title";
    private final String CHALLENGE_PARAM_DESCRIPTION = "challenge_validation_description";

    public ValidationFragment() {
    }

    public static ValidationFragment newInstance(ArrayList<Challenge> list) {
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

        challengeList = (ArrayList<Challenge>) getArguments().getSerializable("Validation");
        if(challengeList == null) {
            challengeList = new ArrayList<>();
        }
        getArguments().remove("Validation");
        //Initialiser l'adapter
        adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, challengeList);
        challengeListView = (ListView) rootView.findViewById(R.id.challenge_list);
        //L'appliquer sur la listView
        challengeListView.setAdapter(adapter);


        challengeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Challenge c = (Challenge) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), PictureValidationActivity.class);
                //TODO: putExtra ID not TITLE/DESCRIPTION
                intent.putExtra(CHALLENGE_PARAM_TITLE, c.getTitle());
                intent.putExtra(CHALLENGE_PARAM_DESCRIPTION, c.getSummary());
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

    public void onListChanged(List<Challenge> newList) {
        if (newList == null) {
            adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, challengeList);
        } else {
            adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, newList);
        }
        challengeListView.setAdapter(adapter);
    }

}