package hexaram.challengelyon.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import hexaram.challengelyon.R;

/**
 * Created by maria on 29/04/15.
 */
public class InscriptionLogin {

    public static InscriptionLogin newInstance() {

        InscriptionLogin l = new InscriptionLogin();
        return l;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_challenge_view, container, false);
        EditText textUserEmail = (EditText) rootView.findViewById(R.id.inscription_view_userEmail);
        CharSequence userEmail = textUserEmail.getText();

        EditText textUserPassword = (EditText) rootView.findViewById(R.id.inscription_view_userPassword);
        CharSequence userPassword = textUserPassword.getText();

        EditText textPasswordConfirm = (EditText) rootView.findViewById(R.id.inscription_view_userPassword);
        CharSequence passwordConfirm = textPasswordConfirm.getText();

        if(!userPassword.equals(passwordConfirm)) {
            String errorText = "Les 2 mots de passe ne correspondent pas";
            TextView errorPasswordMatch = (TextView) rootView.findViewById(R.id.inscription_view_textError);
            errorPasswordMatch.setText(errorText);
        }
        return rootView;
    }


}