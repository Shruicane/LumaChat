package org.luma.client.network;

import Objects.*;
import org.luma.client.frontend.GUI;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    private Socket server;
    private IOHandler ioHandler;
    private GUI gui;
    private Logger log;

    private Thread console;

    private boolean running = true;
    private boolean loggedIn = false;

    private String hostname;
    private final int port;

    private String name;

    public ClientMain(String hostname, int port, GUI gui) {
        this.hostname = hostname;
        this.port = port;

        this.gui = gui;
        log = new Logger(gui);

        log.network("Client >> Started");
        connect();
    }

    public boolean changeIp(String hostname) {
        disconnect("IP Change");
        this.hostname = hostname;
        return connect();
    }

    public boolean sendGroup(String group, String msg) {
        return ioHandler.send(new GroupText(group, name, msg));
    }

    public boolean sendPrivate(String receiver, String msg) {
        return ioHandler.send(new PrivateText(receiver, name, msg));
    }

    public boolean send(RequestObject request) {
        return ioHandler.send(request);
    }

    public boolean register(String name, String password) {
        System.out.println("register " + name + ":" + password);
        if (!isConnected())
            connect();
        if(ioHandler == null){
            return false;
        }
        return ioHandler.send(new Register(name, password));
    }

    public boolean login(String name, String password) {
        return login("login " + name + ":" + password);
    }

    private boolean login(String input) {
        System.out.println(input);
        try {
            String[] acc = input.split(" ")[1].split(":");
            acc[1] = input.split(" ")[1].replaceFirst(acc[0] + ":", "");
            if (loggedIn = login(new Login(acc[0], acc[1]))) {
                name = acc[0];
                return loggedIn;
            }
        } catch (NullPointerException ignored) {
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("CMD >> Wrong Format: login [name]:[password]");
        }
        return false;
    }

    private boolean login(Login login) {
        if (!isConnected())
            connect();
        return ioHandler.send(login);
    }

    private boolean connect() {
        try {
            server = new Socket();
            server.connect(new InetSocketAddress(hostname, port), 1000);
            log.network("Client >> Connected to " + hostname + " on port " + port);
            ioHandler = new IOHandler(server, this, log, gui);
            ioHandler.start();
        } catch (IOException e) {
            try {
                server.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            log.warning("Client >> Server is not reachable");
            return false;
        }
        return true;
    }

    public void disconnect(String msg) {
        if (!isConnected())
            log.error("Client >> Not connected");
        else try {
            loggedIn = false;
            ioHandler.close();
            server.close();
            log.network(msg);
            gui.logout();
        } catch (NullPointerException | IOException ignored) {
        }
    }

    public boolean isConnected() {
        return !server.isClosed();
    }

    public void stop() {
        if (isConnected())
            disconnect("Client >> Disconnected");
        log.network("Client >> Stopping");
        running = false;
        System.exit(0);
    }

    public String getName() {
        return name;
    }

    public GUI getGui() {
        return gui;
    }
}
