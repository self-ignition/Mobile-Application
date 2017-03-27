package comself_ignition.httpsgithub.meetneat.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
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

import static android.R.id.list;
import static android.provider.Telephony.TextBasedSmsColumns.BODY;


public class MessageActivity extends AppCompatActivity implements VolleyCallback {

    String Sender;
    String Recipient;
    Conversation conversation;
    ListView list;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        this.Sender = getIntent().getStringExtra("sender");
        this.Recipient = getIntent().getStringExtra("recipient");
        message = (EditText) findViewById(R.id.messageText);
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
        Log.e("result", "onSuccess: " + result );
        conversation = new Conversation(result, Sender);
        list=(ListView) findViewById(R.id.messageList);
        list.setAdapter(new adapterMessage(this,conversation));
    }

    public void sendMessage(View v) {
        String sendMessage = message.getText().toString();

        ServerRequests ser = new ServerRequests();
        ser.MessageRequest(this, this, MessageAction.SEND, SaveSharedPreference.getUserName(this), Recipient, sendMessage);
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
        String Sender = conversation.getAggreateConversation().get(position).getSender();

        if (Sender.equals(SaveSharedPreference.getUserName(context))) {
            //Not pending request, should have menu button in it
            View row = inflater.inflate(R.layout.activity_message_outgoing_row, parent, false);
            TextView message = (TextView) row.findViewById(R.id.outgoingMessage);
            message.setText(conversation.getAggreateConversation().get(position).getBody());

            return row;
        } else {
            //Pending Friend request
            View row = inflater.inflate(R.layout.activity_message_incoming_row, parent, false);
            TextView message = (TextView) row.findViewById(R.id.incomingMessage);
            message.setText(conversation.getAggreateConversation().get(position).getBody());

            return row;
        }

    }
}
