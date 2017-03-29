package comself_ignition.httpsgithub.meetneat.other;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by r-bur on 27/03/2017.
 */

public class Conversation {
    List<Message> Sent = new ArrayList<>();
    List<Message> Recieved = new ArrayList<>();
    List<Message> aggreateConversation = new ArrayList<>();

    public Conversation(String convesation, String localUsername){
        String[] messages = convesation.split("\\r\\n\\r\\n");

        for (String s: messages) {
            Message m = new Message(s);
            aggreateConversation.add(m);

            if(m.getSender().equals(localUsername)){
                Sent.add(m);
            }
            else
            {
                Recieved.add(m);
            }
        }
    }

    public int Count(){
        return (Sent.size() + Recieved.size());
    }

    public List<Message> getSent() {
        return Sent;
    }

    public List<Message> getRecieved() {
        return Recieved;
    }

    public List<Message> getAggreateConversation() {
        return aggreateConversation;
    }

}
