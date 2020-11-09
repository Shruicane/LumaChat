package org.luma.server.network;

import Objects.Text;

import java.util.LinkedList;

public class MessageListener {
    private final ClientManager cm;

    public MessageListener(ClientManager cm) {
        System.out.println("[org.luma.server.org.luma.network.MessageListener] Started Listener");
        this.cm = cm;
    }

    public void shout(Text text) {
        System.out.println("From: " + text.getSender() + " To: All");
        System.out.println("Message: " + text.getText());
        LinkedList<Client> clients = cm.getOnlineClients();
        System.out.println(clients);
        for(Client client:clients){
            client.send(text);
        }
    }
}
