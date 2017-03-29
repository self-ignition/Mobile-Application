package comself_ignition.httpsgithub.meetneat.other;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by r-bur on 27/03/2017.
 */

public class Message {
    String sender = "";
    String recipient = "";
    String date = "";
    String time = "";
    String body = "";

    public Message(String message)
    {
        parse(message);
    }

    public String getSender() {
        return sender;
    }
    public String getRecipient() {
        return recipient;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getBody() {
        return body;
    }

    public void setSender(String sender) {
        if(sender != null)
            this.sender = sender;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public void setDate(String date) {

        this.date = date;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setBody(String body) {
        if(body != null)
            this.body = body;
    }

    void parse(String message){
        String[] parts = message.split("\\|");
        try {

            setSender(parts[0]);
            setRecipient(parts[1]);
            setTime(parts[2]);
            setDate(parts[3]);
            setBody(parts[4]);
        }
        catch (Exception e)
        {
            Log.e("Message Error", e.getLocalizedMessage());
        }
    }
}
