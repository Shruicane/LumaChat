package org.luma.server.network;

import Objects.Login;

import java.io.IOException;
import java.net.ServerSocket;

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
                Client client = new Client(serverSocket.accept());
                client.setNetworkListener(this);
                cm.addClient(client);
            } catch (IOException ignored) {
            }
        }
    }

    public void disconnectClient(Client client) {
        cm.disconnectClient(client);
    }

    public boolean login(Login login) {
        return cm.login(login);
    }

    public void close() {
        running = false;
        interrupt();
        System.out.println("[NetworkListener] Closed Listener");
    }
}
