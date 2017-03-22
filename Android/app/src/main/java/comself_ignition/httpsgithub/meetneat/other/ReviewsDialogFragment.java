package comself_ignition.httpsgithub.meetneat.other;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;

/**
 * Created by ctrue on 22/03/2017.
 */


public class ReviewsDialogFragment extends DialogFragment {

    String id;

    public ReviewsDialogFragment(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_recipe_review_dialog, container, false);
        getDialog().setTitle("Simple Dialog");
        final Button add = (Button) rootView.findViewById(R.id.submit);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText review = (EditText) rootView.findViewById(R.id.input_addReview);
                String str = review.getText().toString();
                ServerRequests ser = new ServerRequests();
                ser.AddReview(getActivity(), SaveSharedPreference.getUserName(getActivity()), id, str);
                Log.i("TEST: ", str);
                dismiss();
            }
        });

        Button dismiss = (Button) rootView.findViewById(R.id.cancel);
        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }
}
