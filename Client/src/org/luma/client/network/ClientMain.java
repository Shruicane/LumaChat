package org.luma.client.network;

import Objects.Login;
import Objects.Text;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    private Socket server;
    private IOHandler ioHandler;

    private boolean running = true;
    private boolean loggedIn = false;

    private final String hostname;
    private final int port;

    private String name;
    private String password;

    public ClientMain(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;

        connect();
        Scanner scanner = new Scanner(System.in);

        while (running) {
            String input = scanner.nextLine();
            if (input.matches("exit")) {
                disconnect("[Client:ClientMain] disconnected");
            } else if (input.matches("stop")) {
                stop();
            } else if (!loggedIn && input.split(" ")[0].matches("login")) {
                try {
                    String[] acc = input.split(" ")[1].split(":");
                    if(loggedIn = login(new Login(acc[0], acc[1]))){
                        name = acc[0];
                        password = acc[1];
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("[Client:ClientMain] Wrong Format: login <name>:<password>");
                }
            } else if (!input.isEmpty()) {
                boolean success = ioHandler.send(new Text(name, input));
            }
        }
    }

    public boolean login(Login login) {
        if(!isConnected())
            connect();
        return ioHandler.send(login);
    }

    public void connect() {
        try {
            server = new Socket();
            server.connect(new InetSocketAddress(hostname, port), 1000);
            System.out.println("[Client:connect] connected to " + hostname + " on port " + port);
            ioHandler = new IOHandler(server, this);
            ioHandler.start();
        } catch (IOException e) {
            System.out.println("[Client:connect] Server is not reachable");
        }
    }

    public void disconnect(String msg) {
        if (!isConnected())
            System.out.println("[Client:disconnect] not connected");
        else try {
            loggedIn = false;
            ioHandler.close();
            server.close();
            System.out.println(msg);
        } catch (NullPointerException | IOException ignored) {
            System.out.println("Test:disconnect");
        }
    }

    public boolean isConnected() {
        return !server.isClosed();
    }

    public void stop() {
        System.out.println("[Client:stop] stopping");
        running = false;
        if (isConnected())
            disconnect("[Client:stop] disconnected");
    }
}
