package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.FriendAction;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;

import static comself_ignition.httpsgithub.meetneat.activity.MainActivity.navItemIndex;


public class FriendsFragment extends Fragment implements VolleyCallback {
    private FloatingActionButton fab;

    Map<String, Boolean> friends = new HashMap<>();
    ListView list;

    public FriendsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance() {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = (ListView) getActivity().findViewById(R.id.friendsList);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        Context c = getActivity().getApplicationContext();
        ServerRequests sr = new ServerRequests();
        sr.Friends(c, this, FriendAction.getSender, SaveSharedPreference.getUserName(c), "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onSuccess(String result) {
        String[] friends = result.split("�");
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

    private void UpdateList() {
        List<String> names = new ArrayList<>();
        for (String s : friends.keySet()) {
            names.add(s);
        }
        list=(ListView) getActivity().findViewById(R.id.friendsList);
        list.setAdapter(new adapterFriends(getActivity(), names));
    }

    public void onButtonClick(View v) {
        //Do shit here
    }

    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }
}

class adapterFriends extends ArrayAdapter<String> {
    Context context;
    List<String> names;

    adapterFriends(Context c, List<String> names) {
        super(c, R.layout.fragment_notifications_friend_request_row, names);
        this.context = c;
        this.names = names;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.fragment_notifications_friend_request_row, parent, false);
        TextView name = (TextView) row.findViewById(R.id.FriendName);

        name.setText(names.get(position).toString());

        return row;
    }
}