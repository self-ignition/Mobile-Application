package comself_ignition.httpsgithub.meetneat.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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
