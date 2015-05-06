package hexaram.challengelyon.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;

/**
 * Created by William on 29/04/2015.
 */
public class ChallengeAdapter<T> extends ArrayAdapter{

    public ChallengeAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Get the data item for this position
        Challenge challenge = (Challenge) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.challenge_list_item, parent, false);
        }

        // Lookup view for data population
        TextView textItemTitle = (TextView) convertView.findViewById(R.id.challenge_list_item_title);
        TextView textItemSummary = (TextView) convertView.findViewById(R.id.challenge_list_item_summary);
        TextView textItemReward = (TextView) convertView.findViewById(R.id.challenge_list_item_reward);

        textItemTitle.setText(challenge.getTitle());
        textItemSummary.setText(challenge.getSummary());
        textItemReward.setText(""+challenge.getReward());
        // Return the completed view to render on screen
        return convertView;
    }
}
