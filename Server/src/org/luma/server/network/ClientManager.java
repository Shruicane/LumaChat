package org.luma.server.network;

import Objects.Login;
import Objects.Register;
import Objects.SystemText;

import java.util.LinkedList;

public class ClientManager {
    private final LinkedList<Client> allClients = new LinkedList<>();
    private final LinkedList<Client> connectedClients = new LinkedList<>();
    private final LinkedList<Client> onlineClients = new LinkedList<>();

    public void addClient(Client client) {
        Logger.network("ClientManager >> New client connected");
        client.setMessageListener(new MessageListener(this));
        connectedClients.add(client);
        client.start();
    }

    public boolean addSilentClient(String name, String password) {
        if(allClients.contains(new Client(new Login(name, password))))
            return false;
        Client client = new Client(new Login(name, password));
        allClients.add(client);
        return true;
    }

    public void deleteClient(String name, String msg) {
        Client client = findClientFromAll(name);
        if (client == null) {
            Logger.warning("ClientManager >> No client <" + Logger.italic(name) + "> found");
        } else {
            kick(name, msg);
            allClients.remove(client);
        }
    }

    public Client findClient(String name) {
        for (Client client : connectedClients) {
            if (client.checkName(name))
                return client;
        }
        return null;
    }

    public Client findClientFromAll(String name) {
        for (Client client : allClients) {
            if (client.checkName(name))
                return client;
        }
        return null;
    }

    public boolean login(Login login, Client client) {
        //if connectedClients contains client who is logging in - client is already connected
        if (connectedClients.contains(new Client(login))) {
            return false;
        }
        int index = allClients.indexOf(new Client(login));
        //if client is not in allClients or password matches registered Client
        if (index == -1) {
            return false;
        }
        if (allClients.get(index).checkPassword(login.getMessage())) {
            onlineClients.add(client);
            return true;
        }
        return false;
    }

    public boolean register(Register register, Client client){
        int index = allClients.indexOf(new Client(new Login(register.getSender(), register.getMessage())));
        System.out.println(new Login(register.getSender(), register.getMessage()).toString());
        //if client is not in allClients
        if (index == -1) {
            allClients.add(client);
            return true;
        }
        return false;
    }

    public void disconnectClient(Client client) {
        client.close();
        onlineClients.remove(client);
        connectedClients.remove(client);
        if (client.isLoggedIn()) {
            shout(new SystemText("[-] " + client.getName()));
        }
        client.logout();
        Logger.network("NetworkListener >> Client <" + client.getName() + "> disconnected");
    }

    public void kick(String name, String msg) {
        Client client = findClient(name);
        if (client == null) {
            Logger.network("ClientManager >> No client <" + name + "> connected");
        } else {
            client.send(new SystemText(msg));
            disconnectClient(client);
        }
    }

    public void close() {
        for (Client client : new LinkedList<>(connectedClients)) {
            disconnectClient(client);
        }
    }

    public void shout(SystemText text) {
        Logger.message(text.getSender() + " >> All: " + text.getMessage());
        LinkedList<Client> clients = getOnlineClients();
        for (Client client : clients) {
            client.send(text);
        }
    }

    public String formatList(LinkedList<Client> list) {
        StringBuilder sb = new StringBuilder();
        if (!list.isEmpty())
            sb.append(list.get(0).getName());
        for (int i = 1; i < list.size(); i++) {
            sb.append(", ").append(list.get(i).getName());
        }
        return sb.toString();
    }

    public LinkedList<Client> getOnlineClients() {
        return onlineClients;
    }

    public LinkedList<Client> getAllClients() {
        return allClients;
    }

    public LinkedList<Client> getConnectedClients() {
        return connectedClients;
    }
}
