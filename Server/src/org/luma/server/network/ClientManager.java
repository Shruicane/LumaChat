package org.luma.server.network;

import java.util.LinkedList;

public class ClientManager {
    private final LinkedList<Client> allClients = new LinkedList<>();
    private final LinkedList<Client> onlineClients = new LinkedList<>();

    public ClientManager() {
    }

    public void addClient(Client client) {
        client.setMessageListener(new MessageListener(this));
        allClients.add(client);
        onlineClients.add(client);
        client.start();
        System.out.println("[NetworkListener] New client connected");
    }

    public void disconnectClient(Client client) {
        client.close();
        onlineClients.remove(client);
        System.out.println("[NetworkListener] Client <" + client.getName() + "> disconnected");
        System.out.println(onlineClients);
    }

    public LinkedList<Client> getAllClients() {
        LinkedList<Client> clone = new LinkedList<>();
        clone.addAll(allClients);
        return clone;
    }

    public LinkedList<Client> getOnlineClients() {
        LinkedList<Client> clone = new LinkedList<>();
        clone.addAll(onlineClients);
        return clone;
    }
}
