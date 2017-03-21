package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.activity.MainActivity;
import comself_ignition.httpsgithub.meetneat.other.FriendAction;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class FriendsFragment extends Fragment implements VolleyCallback {

    Map<String, Boolean> friends = new HashMap<>();
    ListView list;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton myFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                final MyFriendsDialogFragment dialogFragment = new MyFriendsDialogFragment();
                dialogFragment.show(fm, "Sample Fragment");

                fm.executePendingTransactions();
                dialogFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        onResume();
                    }
                });

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        list = (ListView) getActivity().findViewById(R.id.friendsList);
        Context c = getActivity().getApplicationContext();
        ServerRequests sr = new ServerRequests();
        sr.Friends(c, this, FriendAction.getSender, SaveSharedPreference.getUserName(c), "");
    }

    @Override
    public void onSuccess(String result) {
        String[] friends = result.split("¦");
        try{
            for (String friend : friends) {
                String name = friend.split("\\|")[0];
                Boolean confirmed;
                if(friend.split("\\|")[1].charAt(0) == '1')
                {
                    confirmed = true;
                }
                else {
                    confirmed = false;
                }
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
        Collections.sort(names);
        list=(ListView) getActivity().findViewById(R.id.friendsList);
        list.setAdapter(new adapterFriends(getActivity(),friends, names, this));
    }
}

class adapterFriends extends ArrayAdapter<String> implements VolleyCallback{
    Context context;
    Map<String, Boolean> friends = new HashMap<>();
    List<String> names;
    VolleyCallback callback;

    adapterFriends(Context c, Map<String, Boolean> friends, List<String> names, final VolleyCallback callback) {
        super(c, R.layout.fragment_friends_row, names);
        this.names = names;
        this.context = c;
        this.friends = friends;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);


        if(friends.get(names.get(position))) {
            //Not pending request, should have menu button in it
            View row = inflater.inflate(R.layout.fragment_friends_row, parent, false);
            TextView name = (TextView) row.findViewById(R.id.FriendName);

            final String friendName = friends.keySet().toArray()[position].toString();

            final ImageView menuButton = (ImageView) row.findViewById(R.id.friends_menu_button);
            menuButton.setOnClickListener(new View.OnClickListener() {

                                           @Override
                                           public void onClick(View v) {
                                                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                                               PopupMenu popup = new PopupMenu(context, menuButton);
                                               //Inflating the Popup using xml file
                                               popup.getMenuInflater().inflate(R.menu.friends_menu, popup.getMenu());

                                               //registering popup with OnMenuItemClickListener
                                               popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                   public boolean onMenuItemClick(MenuItem item) {
                                                       switch (item.getItemId())
                                                       {
                                                           case R.id.Message_Button:
                                                               Toast.makeText(context, "Suck me off", Toast.LENGTH_SHORT).show();
                                                               return true;
                                                           case R.id.Remove_Button:
                                                               ServerRequests req = new ServerRequests();
                                                               req.Friends(context, callback, FriendAction.remove, SaveSharedPreference.getUserName(context), friendName);
                                                               return true;
                                                           default:
                                                               return true;
                                                       }
                                                   }
                                               });

                                               popup.show();
                                           }
                                       });

            name.setText(names.get(position).toString());

            return row;
        } else {
            //Pending Friend request
            View row = inflater.inflate(R.layout.fragment_friends_pending_row, parent, false);
            TextView name = (TextView) row.findViewById(R.id.FriendName);

            name.setText(names.get(position).toString());

            return row;
        }
    }

    @Override
    public void onSuccess(String result) {
        Log.i("FOCUS YOU FUCK", "onSuccess: "  + result);
    }
}

class MyFriendsDialogFragment extends DialogFragment implements VolleyCallback {

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