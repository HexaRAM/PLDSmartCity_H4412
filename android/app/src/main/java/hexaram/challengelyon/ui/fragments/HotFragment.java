package hexaram.challengelyon.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;
import hexaram.challengelyon.ui.activities.MainActivity;
import hexaram.challengelyon.ui.adapter.ChallengeAdapter;


public class HotFragment extends Fragment {

    protected ArrayList<Challenge> challengeList;
    protected ChallengeAdapter<Challenge> adapter;
    protected ListView challengeListView;

    public HotFragment() {
    }

    public static HotFragment newInstance(ArrayList<Challenge> list) {
        HotFragment challengeFragment = new HotFragment();
        Bundle args = new Bundle();
        args.putSerializable("Hot", list);
        challengeFragment.setArguments(args);
        Log.d("myTagInstance",list.size()+"");
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

        challengeList = (ArrayList<Challenge>) getArguments().getSerializable("Hot");
        if(challengeList == null) {
            challengeList = new ArrayList<>();
        }
        getArguments().remove("Hot");
        //Initialiser l'adapter
            adapter = new ChallengeAdapter<>(getActivity(), R.layout.challenge_list_item, challengeList);
            challengeListView = (ListView) rootView.findViewById(R.id.challenge_list);
            //L'appliquer sur la listView
            challengeListView.setAdapter(adapter);


//        challengeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Challenge c = (Challenge) parent.getItemAtPosition(position);
////                Intent intent = new Intent(getActivity(), DetailsActivity.class);
////                intent.putExtra("challenge", c);
//////                String transitionName = getString(R.string.challenge_details_transition);
//////                ActivityOptionsCompat options =
//////                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//////                                view,   // The view which starts the transition
//////                                transitionName    // The transitionName of the view we’re transitioning to
//////                        );
//////                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
////            }
//        });
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
