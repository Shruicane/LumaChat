package org.luma.server.network;

import Objects.Text;

import java.util.LinkedList;

public class MessageListener {
    private final ClientManager cm;

    public MessageListener(ClientManager cm) {
        System.out.println("[MessageListener] Started Listener");
        this.cm = cm;
    }

    public void shout(Text text) {
        System.out.println("From: " + text.getSender() + " To: All");
        System.out.println("Message: " + text.getMessage());
        LinkedList<Client> clients = cm.getOnlineClients();
        for(Client client:clients) {
            client.send(text);
        }
    }
}
