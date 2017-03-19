package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.FriendAction;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class NotificationsFragment extends Fragment implements VolleyCallback {
    Map<String, Boolean> friends = new HashMap<>();
    ListView list;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = (ListView) getActivity().findViewById(R.id.friendsList);

        Context c = getActivity().getApplicationContext();
        ServerRequests sr = new ServerRequests();
        sr.Friends(c, this, FriendAction.getRecipient, "", SaveSharedPreference.getUserName(c));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onSuccess(String result) {
        String[] friends = result.split("¦");
        try{
            for (String friend : friends) {
                String name = friend.split("\\|")[0];
                Boolean confirmed = "1".equals(friend.split("\\|")[1].charAt(0));
                this.friends.put(name, confirmed);
            }
            UpdateList();
        } catch(IndexOutOfBoundsException e) {
            Log.e("RESULT", result);
        }
    }
    List<String> names = new ArrayList<>();
    private void UpdateList() {

        for (String s : friends.keySet()) {
            names.add(s);
        }
        list=(ListView) getActivity().findViewById(R.id.friendsList);
        list.setAdapter(new adapterFriendsNotification(getActivity(), names, this));
    }
}

final class adapterFriendsNotification extends ArrayAdapter<String> implements VolleyCallback {
    Context context;
    List<String> names;
    VolleyCallback callback;

    adapterFriendsNotification(Context c, List<String> names, final VolleyCallback callback){
        super(c, R.layout.fragment_notifications_friend_request_row, names);
        this.context = c;
        this.names = names;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.fragment_notifications_friend_request_row, parent, false);
        final TextView name = (TextView) row.findViewById(R.id.FriendName);

        name.setText(names.get(position).toString());

        final String friendName = name.getText().toString();

        ImageView AcceptButton = (ImageView) row.findViewById(R.id.acceptButton);
        AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerRequests req = new ServerRequests();
                req.Friends(context, callback, FriendAction.confirm, friendName, SaveSharedPreference.getUserName(context));
                Toast.makeText(context, "Friend request accepted", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView DeclineButton = (ImageView) row.findViewById(R.id.rejectButton);
        DeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerRequests req = new ServerRequests();
                req.Friends(context, callback, FriendAction.remove, SaveSharedPreference.getUserName(context), friendName);
                Toast.makeText(context, "Friend request declined", Toast.LENGTH_SHORT).show();
            }
        });
        return row;
    }

    @Override
    public void onSuccess(String result) {
        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
    }
}
