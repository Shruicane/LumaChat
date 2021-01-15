package org.luma.server.network;

import Objects.*;
import org.luma.server.database.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class ClientManager {
    Logger log;
    //private final LinkedList<Client> allClients = new LinkedList<>();
    private final LinkedList<Client> connectedClients = new LinkedList<>();
    private final LinkedList<Client> onlineClients = new LinkedList<>();
    UserManagement userManager;
    GroupManagement groupManager;
    IOManagement ioManager;
    MessageManager messageManager;


    public ClientManager(Logger log, IOManagement ioManager, MySQLConnection mySQLConnection, MessageManager messageManager) {
        this.log = log;
        this.messageManager = messageManager;
        userManager = mySQLConnection.getUserManager();
        groupManager = mySQLConnection.getGroupManager();
        this.ioManager = ioManager;
    }

    public void addClient(Client client) {
        log.network("ClientManager >> New client connected");
        client.setMessageListener(new MessageListener(this, log));
        connectedClients.add(client);
        client.start();
    }

    public boolean addSilentClient(String name, String password) {
        if (userManager.userExists(name))
            return false;
        Client client = new Client(new Login(name, password));
        if (userManager.createUser(name, password)) {
            //allClients.add(client);
            return true;
        }
        return false;
    }

    public void deleteClient(String name, String msg) {
        if (userManager.userExists(name)) {
            if (findClient(name) != null)
                kick(name, msg, "Your Account was Deleted!");
            userManager.deleteUser(name);
        } else {
            log.warning("ClientManager >> No client <" + log.italic(name) + "> found");
        }
    }

    public Client findClient(String name) {
        for (Client client : connectedClients) {
            if (client != null && name != null)
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
        if (!userManager.userExists(login.getSender())) {
            return false;
        }
        if (userManager.loginUser(login.getSender(), (String) login.getInformation())) {
            onlineClients.add(client);
            return true;
        }
        return false;
    }

    public boolean register(Register register, Client client) {
        if (userManager.userExists((register.getSender()))) {
            return false;
        }
        //int index = allClients.indexOf(new Client(new Login(register.getSender(), register.getMessage())));
        //if client is not in allClients
        //if (index == -1) {
        //    allClients.add(client);
        //    userManager.createUser(register.getSender(), register.getMessage());
        //    return true;
        //}
        if (userManager.createUser(register.getSender(), (String) register.getInformation())) {
            return true;
        }
        return false;
    }

    public void disconnectClient(Client client) {
        client.close();
        onlineClients.remove(client);
        connectedClients.remove(client);
        if (client.isLoggedIn()) {
            shout(client.getName(), "[-] " + client.getName());
        }
        client.logout();
        log.network("NetworkListener >> Client <" + client.getName() + "> disconnected");
    }

    public void warn(String username, String msg) {
        Client client = findClient(username);
        if (client != null) {
            client.send(new WarnText("You were Warned!", msg));
        }

    }

    public boolean kick(String username, String msg, String type) {
        Client client = findClient(username);
        if (client == null) {
            log.network("ClientManager >> No client <" + username + "> connected");
        } else {
            client.send(new WarnText(type, msg));
            disconnectClient(client);
            return true;
        }
        return false;
    }

    public void ban(String username, String msg) {
        if (isOnline(username))
            kick(username, msg, "You were Banned!");
        userManager.permaBanUser(username);
    }

    public void unban(String username) {
        userManager.removeBan(username);
    }

    public boolean isBanned(String username) {
        return (userManager.isBanned(username) != BanStatus.NONE);
    }

    private boolean isOnline(String username) {
        for (Client client : onlineClients) {
            if (client.checkName(username))
                return true;
        }
        return false;
    }

    public void close() {
        for (Client client : new LinkedList<>(connectedClients)) {
            disconnectClient(client);
        }
    }

    public void shout(String sender, String message) {
        ArrayList<String> groups = userManager.getAllGroups(sender);
        for (String groupName : groups) {
            ArrayList<String> users = groupManager.getAllUsers(groupName);
            for (String user : users) {
                Client client = findClient(user);
                if (client != null) {
                    client.send(new SystemText(groupName, message));

                }
            }
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String datum = formatter.format(date).split(" ")[0];
            String time = formatter.format(date).split(" ")[1];
            messageManager.saveMessage(message, groupName, datum, time, sender);
        }
    }

    public void message(String groupName, String sender, String message) {
        ArrayList<String> users = groupManager.getAllUsers(groupName);
        for (String user : users) {
            Client client = findClient(user);
            if (client != null) {
                client.send(new Text(groupName, sender, message));
            }
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String datum = formatter.format(date).split(" ")[0];
        String time = formatter.format(date).split(" ")[1];
        messageManager.saveMessage(message, groupName, datum, time, sender);
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

    public void sendUpdateInfo(String username, String type, Object data) {
        Client client = findClient(username);
        if (client != null) {
            client.send(new Update(type, "System", data));
        }
    }

    public LinkedList<Client> getOnlineClients() {
        return onlineClients;
    }

    //public LinkedList<Client> getAllClients() {
    //return allClients;
    //}

    public LinkedList<Client> getConnectedClients() {
        return connectedClients;
    }

    public Object getAllGroupsWithUsers(String username) {
        return userManager.getAllGroupsWithUsers(username);
    }
}
