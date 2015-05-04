package hexaram.challengelyon.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import hexaram.challengelyon.R;
import hexaram.challengelyon.models.Challenge;

/**
 * Created by maria on 29/04/15.
 */
public class ViewLogin {

    public static ViewLogin newInstance() {

        ViewLogin l = new ViewLogin();
        return l;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_challenge_view, container, false);
        EditText textUserEmail = (EditText) rootView.findViewById(R.id.login_view_userEmail);
        CharSequence userEmail = textUserEmail.getText();

        EditText textUserPassword = (EditText) rootView.findViewById(R.id.login_view_userPassword);
        CharSequence userPassword = textUserEmail.getText();

        return rootView;
    }


}