package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.activity.LoginActivity;
import comself_ignition.httpsgithub.meetneat.activity.MessageActivity;
import comself_ignition.httpsgithub.meetneat.activity.SearchResultsActivity;
import comself_ignition.httpsgithub.meetneat.other.MessageAction;
import comself_ignition.httpsgithub.meetneat.other.MyFriendsDialogFragment;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MessagesFragment extends Fragment implements VolleyCallback {

    List<String> Recipients = new ArrayList<>();
    ListView list;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = (ListView) getActivity().findViewById(R.id.messageList);
        Context c = getActivity().getApplicationContext();
        ServerRequests sr = new ServerRequests();
        sr.MessageRequest(c, this, MessageAction.POPULATE, SaveSharedPreference.getUserName(c), "", "");
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                doMySearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void doMySearch(String query) {
        Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
        intent.putExtra("query", query);
        getActivity().startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Toast.makeText(getContext(), "Logged out", Toast.LENGTH_LONG).show();

                SaveSharedPreference.setLoggedIn(getActivity(), false);

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSuccess(String result) {
        Log.i("NO", "onSuccess: " + result);
        for (String s : result.split("\\r\\n\\r\\n")) {
            Recipients.add(s);
        }
        ;

        UpdateList();
    }

    private void UpdateList() {
        Collections.sort(Recipients);
        if (Recipients.size() > 0) {
            list = (ListView) getActivity().findViewById(R.id.messageList);
            list.setAdapter(new adapterMessages(getActivity(), Recipients, this));
        }
    }
}

class adapterMessages extends ArrayAdapter<String> implements VolleyCallback {
    Context context;
    List<String> names;
    VolleyCallback callback;

    adapterMessages(Context c, List<String> names, final VolleyCallback callback) {
        super(c, R.layout.fragment_friends_row, names);
        this.names = names;
        this.context = c;
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

        //Not pending request, should have menu button in it
        View row = inflater.inflate(R.layout.fragment_messages_row, parent, false);
        TextView name = (TextView) row.findViewById(R.id.FriendMessageName);

        name.setText(names.get(position).toString());

        //Card View
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("sender", SaveSharedPreference.getUserName(context));
                i.putExtra("recipient", ((TextView) v.findViewById(R.id.FriendMessageName)).getText().toString() );
                context.startActivity(i);
            }
        });

        // Menu Button
        final ImageView menuButton = (ImageView) row.findViewById(R.id.friendsMessage_menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, menuButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.friends_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.Message_Button:
                                return true;
                            case R.id.Remove_Button:
                                ServerRequests req = new ServerRequests();
                                //TODO: Delete Message History
                                return true;
                            default:
                                return true;
                        }
                    }
                });

                popup.show();
            }
        });

        return row;
    }

    @Override
    public void onSuccess(String result) {
        Log.i("FOCUS YOU FUCK", "onSuccess: "  + result);
    }
}