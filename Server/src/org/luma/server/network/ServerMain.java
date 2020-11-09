package org.luma.server.network;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {
    ServerSocket serverSocket;

    private NetworkListener nl;

    int port = 54321;

    public ServerMain() {
        ClientManager cm = new ClientManager();

        start();

        nl = new NetworkListener(cm, serverSocket);

        nl.start();

        while(true);
    }

    private void start(){
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new ServerMain();
    }
}
