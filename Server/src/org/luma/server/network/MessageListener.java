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

    public void shout(Text text) {
        log.message(text.getSender() + " >> All: " + text.getMessage());
        LinkedList<Client> clients = cm.getOnlineClients();
        for(Client client:clients) {
            client.send(text);
        }
    }

    public void shout(SystemText text) {
        cm.shout(text);
    }

    public String getOnlineClients(Client client){
        String name = client.getName();
        client.setName(name + " (You)");
        String output =  cm.formatList(cm.getOnlineClients());
        client.setName(name);
        return output;
    }
}
