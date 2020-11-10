package org.luma.server.network;

import Objects.Login;
import Objects.SystemText;
import Objects.Text;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class NetworkListener extends Thread {
    private final ServerSocket serverSocket;
    private final ClientManager cm;

    private boolean running = true;

    public NetworkListener(ClientManager cm, ServerSocket serverSocket) {
        System.out.println("[NetworkListener] Started Listener");
        this.cm = cm;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (running) {
            try {
                connectClient(new Client(serverSocket.accept()));
            } catch (IOException ignored) {
                // Timeout after 100ms
            }
        }
    }

    private void connectClient(Client client) {
        client.setNetworkListener(this);
        cm.addClient(client);
    }

    public void disconnectClient(Client client) {
        cm.disconnectClient(client);
        if(client.isLoggedIn())
            shout(new SystemText("[-] " + client.getName()));
    }

    public boolean login(Login login, Client client) {
        return cm.login(login, client);
    }

    public void shout(SystemText text) {
        System.out.println("From: " + text.getSender() + " To: All");
        System.out.println("Message: " + text.getMessage());
        LinkedList<Client> clients = cm.getOnlineClients();
        for (Client client : clients) {
            client.send(text);
        }
    }

    public void close() {
        running = false;
        interrupt();
        System.out.println("[NetworkListener] Closed Listener");
    }
}
