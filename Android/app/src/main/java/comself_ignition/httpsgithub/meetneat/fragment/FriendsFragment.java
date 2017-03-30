package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.activity.MainActivity;
import comself_ignition.httpsgithub.meetneat.activity.MessageActivity;
import comself_ignition.httpsgithub.meetneat.activity.SearchResultsActivity;
import comself_ignition.httpsgithub.meetneat.other.*;

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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_addFriend:
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        //names = sorted list of keys
        //friends = dictionary
        super(c, R.layout.fragment_friends_row, names);
        //set map alphabetically
        //
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);


        if(friends.get(names.get(position))) {
            //Not pending request, should have menu button in it
            View row = inflater.inflate(R.layout.fragment_friends_row, parent, false);
            final TextView name = (TextView) row.findViewById(R.id.FriendName);

            final String friendName = names.get(position);
            name.setText(names.get(position).toString());

            final ImageView menuButton = (ImageView) row.findViewById(R.id.friends_menu_button);
            menuButton.setOnClickListener(new View.OnClickListener() {

                                           @Override
                                           public void onClick(final View v) {
                                               //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                                               PopupMenu popup = new PopupMenu(context, menuButton);
                                               //Inflating the Popup using xml file
                                               popup.getMenuInflater().inflate(R.menu.friends_menu, popup.getMenu());

                                               //registering popup with OnMenuItemClickListener
                                               popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                   public boolean onMenuItemClick(MenuItem item) {
                                                       switch (item.getItemId())
                                                       {
                                                           case R.id.Message_Button:
                                                               //Toast.makeText(context, "Suck me off", Toast.LENGTH_SHORT).show();
                                                               Intent i = new Intent(context, MessageActivity.class);
                                                               i.putExtra("sender", SaveSharedPreference.getUserName(context));
                                                               i.putExtra("recipient", name.getText().toString());
                                                               context.startActivity(i);
                                                               return true;
                                                           case R.id.Remove_Button:
                                                               ServerRequests req = new ServerRequests();
                                                               req.Friends(context, callback, FriendAction.remove, SaveSharedPreference.getUserName(context), friendName);
                                                               req.MessageRequest(context, callback, MessageAction.DELETE, SaveSharedPreference.getUserName(context),
                                                                       friendName, "");
                                                               names.remove(friendName);
                                                               notifyDataSetChanged();
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
        Log.i("onSuccess", "onSuccess: "  + result);
    }
}

