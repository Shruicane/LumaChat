package org.luma.client.network;

import Objects.Get;
import Objects.Login;
import Objects.Text;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
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

        Logger.network("Client >> Started");
        connect();

        Scanner scanner = new Scanner(System.in);

        while (running) {
            String input = scanner.nextLine();

            if (input.matches("stop")) {
                stop();
            } else if (input.matches("exit")) {
                disconnect("Client >> Disconnected");
            } else if (!loggedIn && input.split(" ")[0].matches("login")) {
                login(input);
            } else if (input.matches("help")) {
                Logger.cmd("help >> login [name]:[password]");
                Logger.cmd("help >> stop");
                Logger.cmd("help >> exit");
                Logger.cmd("help >> list");
            } else if (!isConnected()) {
                Logger.error("Client >> Not connected");
            } else if (input.matches("list")) {
                Logger.cmd("list >> Online Users: " + ioHandler.send(new Get("onlineClients")));
            } else if (!input.isEmpty()) {
                boolean success = ioHandler.send(new Text(name, input));
            }
        }
    }

    private void login(String input) {
        try {
            String[] acc = input.split(" ")[1].split(":");
            if (loggedIn = login(new Login(acc[0], acc[1]))) {
                name = acc[0];
                password = acc[1];
            }
        } catch (NullPointerException ignored) {
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.error("CMD >> Wrong Format: login [name]:[password]");
        }
    }

    private boolean login(Login login) {
        if (!isConnected())
            connect();
        return ioHandler.send(login);
    }

    private void connect() {
        try {
            server = new Socket();
            server.connect(new InetSocketAddress(hostname, port), 1000);
            Logger.network("Client >> Connected to " + hostname + " on port " + port);
            ioHandler = new IOHandler(server, this);
            ioHandler.start();
        } catch (IOException e) {
            try {
                server.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Logger.warning("Client >> Server is not reachable");
        }
    }

    public void disconnect(String msg) {
        if (!isConnected())
            Logger.error("Client >> Not connected");
        else try {
            loggedIn = false;
            ioHandler.close();
            server.close();
            Logger.network(msg);
        } catch (NullPointerException | IOException ignored) {
        }
    }

    public boolean isConnected() {
        return !server.isClosed();
    }

    private void stop() {
        if (isConnected())
            disconnect("Client >> Disconnected");
        Logger.network("Client >> Stopping");
        running = false;
    }
}
