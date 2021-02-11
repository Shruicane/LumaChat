package org.luma.server.network;

import Objects.Login;
import Objects.Register;
import Objects.RequestObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class NetworkListener extends Thread {
    private final ServerSocket serverSocket;
    private final ClientManager cm;
    private final Logger log;

    private boolean running = true;

    public NetworkListener(ClientManager cm, ServerSocket serverSocket, Logger log) {
        log.network("NetworkListener >> Started Listener");
        this.cm = cm;
        this.serverSocket = serverSocket;
        this.log = log;
    }

    @Override
    public void run() {
        while (running) {
            try {
                connectClient(new Client(serverSocket.accept(), log));
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
    }

    public boolean login(Login login, Client client) {
        return cm.login(login, client);
    }

    public boolean register(Register register, Client client){
        return cm.register(register, client);
    }

    public void sendAll(RequestObject request){
        cm.sendAll(request);
    }

    public Object getAllGroupsWithUser(String username){
        return cm.getAllGroupsWithUser(username);
    }

    public Object getAllChatsFromUser(String username){
        return cm.getAllChatsFromUser(username);
    }

    public Object getAllOnlineClients() {
        return cm.getAllOnlineClients();
    }

    public void close() {
        running = false;
        interrupt();
        log.network("NetworkListener >> Closed Listener");
    }
}
