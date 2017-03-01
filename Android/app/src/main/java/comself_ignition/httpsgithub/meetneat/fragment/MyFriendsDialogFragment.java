package comself_ignition.httpsgithub.meetneat.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.FriendAction;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

import static android.support.design.R.id.text;
import static comself_ignition.httpsgithub.meetneat.R.id.name;

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
        dismiss();
    }
}
