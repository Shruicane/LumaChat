package org.luma.server.network;

import Objects.Login;

import java.util.LinkedList;

public class ClientManager {
    private final LinkedList<Client> allClients = new LinkedList<>();
    private final LinkedList<Client> connectedClients = new LinkedList<>();

    private final LinkedList<Client> onlineClients = new LinkedList<>();

    public ClientManager() {

    }

    public void addClient(Client client) {
        System.out.println("[ClientManager] New client connected");
        client.setMessageListener(new MessageListener(this));
        connectedClients.add(client);
        client.start();
    }

    public boolean login(Login login, Client client) {
        //if connectedClients contains client who is logging in - client is already connected
        if (connectedClients.contains(new Client(login))) {
            return false;
        }
        int index = allClients.indexOf(new Client(login));
        //if client is not in allClients or password matches registered Client
        if (index == -1 || allClients.get(index).checkPassword(login.getMessage())) {
            allClients.add(client);
            onlineClients.add(client);
            return true;
        }
        return false;
    }

    public void disconnectClient(Client client) {
        client.close();
        onlineClients.remove(client);
        connectedClients.remove(client);
        System.out.println("[NetworkListener] Client <" + client.getName() + "> disconnected");
    }

    public LinkedList<Client> getOnlineClients() {
        return onlineClients;
    }
}
