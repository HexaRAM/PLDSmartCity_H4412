package hexaram.challengelyon.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;
import hexaram.challengelyon.models.User;

/**
 * Created by maria on 29/04/15.
 */
public class ChallengeViewFragment extends Fragment {
    public static ChallengeViewFragment newInstance(Challenge challenge) {

        ChallengeViewFragment f = new ChallengeViewFragment();
        //Bundle allows you to pass parameters
        Bundle args = new Bundle();
        args.putSerializable("challenge", challenge);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenge_view, container, false);
        Bundle args = this.getArguments();
        TextView textCreatorName = (TextView) rootView.findViewById(R.id.challenge_view_creator);
        TextView textDescription = (TextView) rootView.findViewById(R.id.challenge_view_description);
        TextView textTitle = (TextView) rootView.findViewById(R.id.challenge_view_title);
        TextView textStartTime = (TextView) rootView.findViewById(R.id.challenge_view_starttime);
        TextView textEndTime = (TextView) rootView.findViewById(R.id.challenge_view_endtime);
        TextView textCategory = (TextView) rootView.findViewById(R.id.challenge_view_category);
        TextView textValidation = (TextView) rootView.findViewById(R.id.challenge_view_validation);

        Challenge challenge = (Challenge) args.getSerializable("challenge");
        textDescription.setText(challenge.getSummary());
        textCreatorName.setText(challenge.getCreator());
        textTitle.setText(challenge.getTitle());
        textStartTime.setText(challenge.getStarttime());
        textEndTime.setText(challenge.getEndtime());
        textCategory.setText(challenge.getCategory().getName());
        //TODO VALIDATION
        return rootView;
    }


}
