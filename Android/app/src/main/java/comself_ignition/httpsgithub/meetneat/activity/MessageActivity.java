package comself_ignition.httpsgithub.meetneat.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.Conversation;
import comself_ignition.httpsgithub.meetneat.other.FriendAction;
import comself_ignition.httpsgithub.meetneat.other.MessageAction;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;
import comself_ignition.httpsgithub.meetneat.other.Message;


public class MessageActivity extends AppCompatActivity implements VolleyCallback {

    String Sender;
    String Recipient;
    Conversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        this.Sender = getIntent().getStringExtra("sender");
        this.Recipient = getIntent().getStringExtra("recipient");
    }

    @Override
    public void onResume() {
        super.onResume();
        Context c = this;
        ServerRequests sr = new ServerRequests();
        sr.MessageRequest(c, this, MessageAction.GET, Sender, Recipient, "");
    }

    @Override
    public void onSuccess(String result) {
        conversation = new Conversation(result, Sender);
    }
}

class adapterMessage extends ArrayAdapter<Message> {
    Conversation conversation;
    Context context;
    String Sender;

    adapterMessage(Context c, Conversation conversation) {
        super(c, R.layout.activity_search_results_row, conversation.getAggreateConversation());
        this.context = c;
        this.conversation = conversation;
        this.Sender = SaveSharedPreference.getUserName(c);
    }

    @Override
    public int getCount() {
        return conversation.Count();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (conversation.getAggreateConversation().get(position).getSender().equals(Sender)) {
            //Not pending request, should have menu button in it
            View row = inflater.inflate(R.layout.activity_message_outgoing_row, parent, false);
            TextView message = (TextView) row.findViewById(R.id.incomingMessage);
            message.setText(conversation.getAggreateConversation().get(position).getBody().toString());


            return row;
        } else {
            //Pending Friend request
            View row = inflater.inflate(R.layout.activity_message_incoming_row, parent, false);
            TextView message = (TextView) row.findViewById(R.id.outgoingMessage);
            message.setText(conversation.getAggreateConversation().get(position).getBody().toString());

            return row;
        }

    }
}
