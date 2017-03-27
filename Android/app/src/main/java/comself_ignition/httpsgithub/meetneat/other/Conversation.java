package comself_ignition.httpsgithub.meetneat.other;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by r-bur on 27/03/2017.
 */

public class Conversation {
    List<Message> Sent = new ArrayList<>();
    List<Message> Recieved = new ArrayList<>();

    public Conversation(String convesation, String localUsername){
        String[] messages = convesation.split("\\r\\n\\r\\n");

        for (String s: messages) {
            if(s.split("\\|")[0].equals(localUsername)){
                Sent.add(new Message(s));
            }
            else
            {
                Recieved.add(new Message(s));
            }
        }
    }
}
