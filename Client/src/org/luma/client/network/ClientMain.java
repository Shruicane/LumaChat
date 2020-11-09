package org.luma.client.network;

import Objects.Text;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    private Socket server;
    private InputHandler iHandler;

    private boolean running = true;

    public ClientMain(String name, String password) {
        connect();
        Scanner scanner = new Scanner(System.in);

        while (running) {
            String input = scanner.nextLine();
            if (input.matches("exit")) {
                disconnect();
            } else if (input.matches("stop")) {
                stop();
            } else if (input.matches("login")) {
                connect();
            } else {
                iHandler.send(new Text(name, input));
            }
        }
    }

    public void connect() {
        try {
            server = new Socket();
            server.connect(new InetSocketAddress("localhost", 54321), 1000);
            System.out.println("[Client] connected to \"localhost\" on port 54321");
            iHandler = new InputHandler(server, this);
            iHandler.start();
        } catch (IOException e) {
            System.out.println("[Client] Server is not reachable");
        }
    }

    public void disconnect() {
        if (!isConnected())
            System.out.println("[Client] not connected");
        else try {
            iHandler.close();
            System.out.println("[Client] disconnected");
        } catch (NullPointerException ignored) {
        }
    }

    public boolean isConnected() {
        return !server.isClosed();
    }

    public void stop() {
        System.out.println("[Client] stopping");
        running = false;
        if(isConnected())
            disconnect();
    }
}
