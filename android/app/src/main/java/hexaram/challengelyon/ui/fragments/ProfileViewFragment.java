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
import hexaram.challengelyon.models.User;

/**
 * Created by William on 28/04/2015.
 */
public class ProfileViewFragment extends Fragment {

    public static ProfileViewFragment newInstance(User user) {
        /*
        * Replace the usual constructor
        */
        ProfileViewFragment f = new ProfileViewFragment();
        //Bundle allows you to pass parameters
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        f.setArguments(args);


        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_view, container, false);
        Bundle args = this.getArguments();
        ImageView imageItem = (ImageView) rootView.findViewById(R.id.profile_view_picture);
        TextView textItemName = (TextView) rootView.findViewById(R.id.profile_view_username);
        TextView textItemMail = (TextView) rootView.findViewById(R.id.profile_view_mail);
        TextView textItemAddress = (TextView) rootView.findViewById(R.id.profile_view_address);
        TextView textItemNbPlayed = (TextView) rootView.findViewById(R.id.profile_view_nbPlayed);
        TextView textItemScore = (TextView) rootView.findViewById(R.id.profile_view_score);
        TextView textItemRank = (TextView) rootView.findViewById(R.id.profile_view_rank);

        User user = (User) args.getSerializable("user");
//        imageItem.setImageDrawable(user.getImage());
        textItemName.setText(user.getUsername());
        textItemMail.setText(user.getMail());
        textItemAddress.setText(user.getAddress());
        textItemNbPlayed.setText(user.getNbPlayed());
        textItemScore.setText(user.getScore());
        textItemRank.setText(user.getRank());


        return rootView;
    }


}

