package org.luma.server.network;

import Objects.SystemText;
import Objects.Text;

import java.util.LinkedList;

public class MessageListener {
    private final ClientManager cm;
    private Logger log;

    public MessageListener(ClientManager cm, Logger log) {
        log.network("MessageListener >> Started Listener");
        this.cm = cm;
        this.log = log;
    }

    public void message(String group, String sender, String message){
        cm.message(group, sender, message);
    }

    public void shout(String sender, String message) {
        cm.shout(sender, message);
    }

    public String getOnlineClients(Client client){
        String name = client.getName();
        client.setName(name + " (You)");
        String output =  cm.formatList(cm.getOnlineClients());
        client.setName(name);
        return output;
    }
}
