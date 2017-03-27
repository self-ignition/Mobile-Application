package comself_ignition.httpsgithub.meetneat.other;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by r-bur on 27/03/2017.
 */

public class Message {
    String sender = "";
    String recipient = "";
    Date date = new Date();
    Date time = new Date();
    String body = "";

    Message(String message)
    {
        parse(message);
    }

    public String getSender() {
        return sender;
    }
    public String getRecipient() {
        return recipient;
    }
    public Date getDate() {
        return date;
    }
    public Date getTime() {
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
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        try {
            if(date != null)
                this.date = df.parse(date);
        } catch (ParseException e) {
            Log.e("Error Setting Date", "setDate: " + e.getLocalizedMessage() );
        }
    }
    public void setTime(String time) {
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        try {
            if(time != null)
                this.time = df.parse(time);
        } catch (ParseException e) {
            Log.e("Error Setting Date", "setDate: " + e.getLocalizedMessage() );
        }
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
            setDate(parts[2]);
            setTime(parts[3]);
            setBody(parts[4]);
        }
        catch (Exception e)
        {
            Log.e("Message Error", e.getLocalizedMessage());
        }
    }
}
