package org.luma.server.network;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
    ServerSocket serverSocket;

    public ServerMain() {
        System.out.println("[Server] Starting");
        long startTime = System.currentTimeMillis();
        ClientManager cm = new ClientManager();

        start();

        NetworkListener nl = new NetworkListener(cm, serverSocket);

        nl.start();

        System.out.println("[Server] Done! (" + (System.currentTimeMillis() - startTime) + "ms)");
        while (true) ;
    }

    private void start() {
        try {
            serverSocket = new ServerSocket(54321);
            serverSocket.setSoTimeout(100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServerMain();
    }
}
