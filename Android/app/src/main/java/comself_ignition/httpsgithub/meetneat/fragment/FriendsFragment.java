package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.*;
import comself_ignition.httpsgithub.meetneat.other.FriendAction;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;



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

        Context c = getActivity().getApplicationContext();
        ServerRequests sr = new ServerRequests();
        sr.Friends(c, this, FriendAction.get, SaveSharedPreference.getUserName(c), "Susan_Boyle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onSuccess(String result) {
        String[] friends = result.split("¦");

        for (String friend: friends) {
            String name = friend.split("\\|")[0];
            Boolean confirmed = "1".equals(friend.split("\\|")[1].charAt(0));
            this.friends.put(name, confirmed);
        }

        UpdateList();
    }

    private void UpdateList() {
        List<String> names = new ArrayList<>();
        for (String s: friends.keySet()) {
            names.add(s);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.friends_list_row, R.id.FriendName, names);

        list.setAdapter(adapter);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shit here
            }

        });
    }

    public void onButtonClick(View v) {
        //Do shit here
    }
}
