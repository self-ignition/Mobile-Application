package comself_ignition.httpsgithub.meetneat.other;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import comself_ignition.httpsgithub.meetneat.R;

/**
 * Created by ctrue on 22/03/2017.
 */

public class MyFriendsDialogFragment extends DialogFragment implements VolleyCallback {

    VolleyCallback callback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_friend, container, false);
        getDialog().setTitle("Simple Dialog");
        final Button add = (Button) rootView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText name = (EditText) rootView.findViewById(R.id.input_addFriend);
                String str = name.getText().toString();
                ServerRequests ser = new ServerRequests();
                ser.Friends(getActivity(), callback, FriendAction.add, SaveSharedPreference.getUserName(getActivity()), str );
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

    @Override
    public void onSuccess(String result) {
        Toast.makeText(getActivity(),"Request sent", Toast.LENGTH_SHORT).show();
    }
}