package org.luma.server.network;

import Objects.SystemText;
import Objects.Text;

import java.util.LinkedList;

public class MessageListener {
    private final ClientManager cm;

    public MessageListener(ClientManager cm) {
        Logger.network("MessageListener >> Started Listener");
        this.cm = cm;
    }

    public void shout(Text text) {
        Logger.message(text.getSender() + " >> All: " + text.getMessage());
        LinkedList<Client> clients = cm.getOnlineClients();
        for(Client client:clients) {
            client.send(text);
        }
    }

    public void shout(SystemText text) {
        cm.shout(text);
    }

    public String getOnlineClients(){
        return cm.formatList(cm.getOnlineClients());
    }
}
