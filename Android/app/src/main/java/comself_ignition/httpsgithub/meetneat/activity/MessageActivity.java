package comself_ignition.httpsgithub.meetneat.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.Conversation;
import comself_ignition.httpsgithub.meetneat.other.Message;
import comself_ignition.httpsgithub.meetneat.other.MessageAction;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

public class MessageActivity extends AppCompatActivity implements VolleyCallback {

    //Global variables used throughout - CT
    String Sender;
    String Recipient;
    Conversation conversation;
    EditText message;
    adapterMessage adapterMessage;
    RecyclerView messageRecyclerView;
    Handler mHandler = new Handler();
    String result;
    List<Message> messageList;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sets up the page - CT
        setContentView(R.layout.activity_message);

        //Pulls the sender and recipient from the previous page, passes them into global variables - CT
        this.Sender = getIntent().getStringExtra("sender");
        this.Recipient = getIntent().getStringExtra("recipient");

        //Sets the title to the username the messages are from - CT
        TextView user = (TextView) findViewById(R.id.usernameMessage);
        user.setText(this.Recipient);

        //Retrieves the messages from the database - CT
        ServerRequests sr = new ServerRequests();
        sr.MessageRequest(this, this, MessageAction.GET, Sender, Recipient, "");

        //Sets up a new thread to run every 5 seconds...used to check for incoming messages - CT
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(5000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                               updateList();
                            }
                        });
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        }).start();
    }

    public void updateList() {
        //Gets the data from the database - CT
        ServerRequests sr = new ServerRequests();
        sr.MessageRequest(this, this, MessageAction.GET, Sender, Recipient, "");
    }

    @Override
    public void onSuccess(String result) {

        //Will only run if the new result is different to the previous result - CT
        if(!result.equals(getResult())) {
            //Set the new result
            setResult(result);

            //Begin setting up the recycler view - CT
            conversation = new Conversation(result, Sender);
            messageRecyclerView = (RecyclerView)findViewById(R.id.messageList);
            messageList = conversation.getAggreateConversation();

            //Handles how the layout will look - CT
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
            linearLayoutManager.setStackFromEnd(true);
            linearLayoutManager.scrollToPosition(messageList.size() - 1);

            //Sets the layout to the recycler view - CT
            messageRecyclerView.setLayoutManager(linearLayoutManager);
            messageRecyclerView.setItemAnimator(new DefaultItemAnimator());

            //Attaches the adapter to the recycler view - CT
            adapterMessage = new adapterMessage(this, messageList);
            messageRecyclerView.setAdapter(adapterMessage);
        }

    }

    public void sendMessage(View v) {
        //Hides the keyboard once a message is sent - CT
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        //Receives the current date and time - CT
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        String today = dateFormat.format(date);
        String time = timeFormat.format(date);

        //Receives the string input from the keyboard into the EditText variable - CT
        String normalMessage = message.getText().toString();

        //Strings together requirements for an entire message - CT
        String messageString = Sender + "|" + Recipient + "|" + time + "|" + today + "|" + normalMessage;
        message.setText("");

        //Add the message to the list and updates the adapter with the new message included - CT
        Message message = new Message(messageString);
        adapterMessage.add(message);
        adapterMessage.notifyDataSetChanged();

        //Sends the message to the database - CT
        ServerRequests ser = new ServerRequests();
        ser.MessageRequest(this, this, MessageAction.SEND, SaveSharedPreference.getUserName(this), Recipient, normalMessage);
    }
}

class adapterMessage extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Message> message;
    Context context;
    String Sender;
    public static final int OUTGOING = 0;
    public static final int INCOMING = 1;

    adapterMessage(Context c, List<Message> message) {
        this.context = c;
        this.message = message;
        this.Sender = SaveSharedPreference.getUserName(c);
    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case OUTGOING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_message_outgoing_row, parent, false);
                return new outgoingHolder(view);
            case INCOMING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_message_incoming_row, parent, false);
                return new incomingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String Sender = message.get(position).getSender();
        String messageBody = message.get(position).getBody();
        String messageDate = message.get(position).getDate();
        String messageTime = message.get(position).getTime();

        if (Sender.equals(SaveSharedPreference.getUserName(context))) {
            ((outgoingHolder)holder).Message.setText(messageBody);
            ((outgoingHolder)holder).time.setText(messageTime + " " + messageDate);
        } else {
            ((incomingHolder)holder).Message.setText(messageBody);
            ((incomingHolder)holder).time.setText(messageTime + " " + messageDate);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String Sender = message.get(position).getSender();
        int viewType;
        if (Sender.equals(SaveSharedPreference.getUserName(context))) {
            viewType = OUTGOING;
        } else {
            viewType = INCOMING;
        }
        return viewType;
    }

    public class outgoingHolder extends RecyclerView.ViewHolder {
        TextView Message;
        TextView time;
        public outgoingHolder(View itemView) {
            super(itemView);
            Message = (TextView) itemView.findViewById(R.id.outgoingMessage);
            time = (TextView) itemView.findViewById(R.id.outgoingTime);
        }
    }

    public class incomingHolder extends RecyclerView.ViewHolder {
        TextView Message;
        TextView time;
        public incomingHolder(View itemView) {
            super(itemView);
            Message = (TextView) itemView.findViewById(R.id.incomingMessage);
            time = (TextView) itemView.findViewById(R.id.incomingTime);
        }
    }

    public void add(Message message2) {
        message.add(message2);
    }
}