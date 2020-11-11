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
        Logger.network("NetworkListener >> Started Listener");
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
    }

    public boolean login(Login login, Client client) {
        return cm.login(login, client);
    }

    public void close() {
        running = false;
        interrupt();
        Logger.network("NetworkListener >> Closed Listener");
    }
}
