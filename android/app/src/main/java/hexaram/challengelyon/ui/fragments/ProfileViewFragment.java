package hexaram.challengelyon.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.User;
import hexaram.challengelyon.services.requestAPI;

/**
 * Created by William on 28/04/2015.
 */
public class ProfileViewFragment extends Fragment {

    User user;
    DrawerLayout mDrawerLayout;


    TextView textItemName;

    TextView textItemScore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_view, container, false);


        textItemName = (TextView) rootView.findViewById(R.id.profile_author_text);
        textItemScore = (TextView) rootView.findViewById(R.id.profile_score_text);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String email = prefs.getString("email","no_email");
        int score = prefs.getInt("score",0);

        textItemName.setText(email);
        textItemScore.setText(""+score);

       /* try {
            Log.d("score", ""+"Log1");
            JSONObject response = req.getUser();
            Log.d("score", ""+"Log2");
            String email = response.getString("email");
            Log.d("score", ""+"Log3");
            int score = response.getInt("ranking");
            Log.d("score", ""+score);
            //User user1 = new User(email,score);
            textItemName.setText(email);
            textItemScore.setText(score);        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/




        Log.d("Profile","Lauchend");
        /*Bundle args = this.getArguments();
        ImageView imageItem = (ImageView) rootView.findViewById(R.id.profile_view_picture);
        TextView textItemName = (TextView) rootView.findViewById(R.id.profile_view_username);
        TextView textItemMail = (TextView) rootView.findViewById(R.id.profile_view_mail);
        TextView textItemAddress = (TextView) rootView.findViewById(R.id.profile_view_address);
        TextView textItemNbPlayed = (TextView) rootView.findViewById(R.id.profile_view_nbPlayed);
        TextView textItemScore = (TextView) rootView.findViewById(R.id.profile_view_score);
        TextView textItemRank = (TextView) rootView.findViewById(R.id.profile_view_rank);*/

        //User user = (User) args.getSerializable("user");
//        imageItem.setImageDrawable(user.getImage());

        return rootView;
    }

    public void setUp(User user, DrawerLayout drawerLayout){
        this.user = user;
        textItemName.setText(user.getUsername());
        textItemScore.setText(user.getScore());
        mDrawerLayout = drawerLayout;
    }


}

