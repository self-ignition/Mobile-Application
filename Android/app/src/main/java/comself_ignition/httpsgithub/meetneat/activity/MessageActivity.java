package comself_ignition.httpsgithub.meetneat.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.FriendAction;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;


public class MessageActivity extends AppCompatActivity implements VolleyCallback {

    String Sender;
    String Recipient;

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
        sr.MessageRequest()
    }

    @Override
    public void onSuccess(String result) {

    }
}
