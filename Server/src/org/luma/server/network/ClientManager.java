package org.luma.server.network;

import Objects.Login;

import java.util.LinkedList;

public class ClientManager {
    private final LinkedList<Client> allClients = new LinkedList<>();
    private final LinkedList<Client> onlineClients = new LinkedList<>();

    public ClientManager() {

    }

    public void addClient(Client client) {
        System.out.println("[NetworkListener] New client connected");
        client.setMessageListener(new MessageListener(this));
        allClients.add(client);
        onlineClients.add(client);
        client.start();
    }

    public boolean login(Login login){
        Client client = new Client(login);
        if(onlineClients.contains(client))
            return false;
        int index = allClients.indexOf(client);
        if(index == -1)
            return true;
        return allClients.get(index).checkPassword(login.getMessage());
    }

    public void disconnectClient(Client client) {
        client.close();
        onlineClients.remove(client);
        System.out.println("[NetworkListener] Client <" + client.getName() + "> disconnected");
    }

    public LinkedList<Client> getOnlineClients() {
        return onlineClients;
    }
}
