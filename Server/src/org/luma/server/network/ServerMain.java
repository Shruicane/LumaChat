package org.luma.server.network;

import org.luma.server.database.IOManagement;
import org.luma.server.database.MessageManager;
import org.luma.server.database.MySQLConnection;
import org.luma.server.database.MySQLDataBase;
import org.luma.server.frontend.controller.Controller;
import org.luma.server.settings.Settings;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Scanner;

public class ServerMain {
    private ServerSocket serverSocket;
    private NetworkListener nl;
    private final ClientManager cm;
    private final Logger log;

    private boolean running = true;

    public ServerMain(Controller controller, IOManagement ioManager, MySQLConnection mySQLConnection) {
        long startTime = System.currentTimeMillis();
        log = new Logger(controller, mySQLConnection.getMysqlDatabase());
        cm = new ClientManager(log, ioManager, mySQLConnection);
        start();
        log.network("Server >> Done! (" + (System.currentTimeMillis() - startTime) + "ms)");
    }

    private void start() {
        if (isAlive())
            log.warning("Server >> Already running!");
        else {
            try {
                running = true;
                log.network("Server >> Starting");
                serverSocket = new ServerSocket(54321);
                serverSocket.setSoTimeout(100);
                nl = new NetworkListener(cm, serverSocket, log);
                nl.start();
            } catch (SocketException e) {
                log.error("Server >> Address already in use");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        close();
        log.network("Server >> Stopping");
        running = false;
    }

    private void close() {
        if (isAlive()) {
            try {
                log.network("Server >> Closing");
                serverSocket.close();
                cm.close();
                nl.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            log.warning("Server >> Already closed");
    }

    private void help() {
        log.cmd("help >> start");
        log.cmd("help >> restart");
        log.cmd("help >> stop");
        log.cmd("help >> exit");
        log.cmd("help >> list all/connected/online");
        log.cmd("help >> kick [name] <reason>");
        log.cmd("help >> edit name/passw [name] [string]");
        log.cmd("help >> new [name]:[password]");
        log.cmd("help >> delete [name] <reason>");
    }

    private void list(String input) {
        String[] args = input.split(" ");
        if (args.length > 1 && args[1].matches("all")) {
            //log.cmd("list all >> " + cm.getAllClients().size() + " User registered:");
            //log.cmd("list all >> " + cm.formatList(cm.getAllClients()));
        } else if (args.length > 1 && args[1].matches("connected")) {
            log.cmd("list connected >> " + cm.getConnectedClients().size() + " User connected:");
            log.cmd("list connected >> " + cm.formatList(cm.getConnectedClients()));
        } else if (args.length > 1 && args[1].matches("online")) {
            log.cmd("list online >> " + cm.getOnlineClients().size() + " User online:");
            log.cmd("list online >> " + cm.formatList(cm.getOnlineClients()));
        } else {
            log.error("CMD >> Wrong Format: list all/connected/online");
        }
    }

    private void kick(String input) {
        String[] args = input.split(" ");
        if (args.length > 1) {
            String extra = input.replace(args[0] + " " + args[1], "");
            cm.kick(args[1], "Kicked by Server" + (args.length > 2 ? " - Reason:" + extra : ""), "You were Kicked!");
        } else {
            log.error("CMD >> Wrong Format: kick [name] <reason>");
        }
    }

    private void newC(String input) {
        String[] args = input.split(" ");
        if (args.length == 2 && args[1].contains(":")) {
            args = args[1].split(":");
            args[1] = input.split(" ")[1].replaceFirst(args[0] + ":", "");
            //String[] args2 = args[1].split(":");
            //args2[1] = args[1].replaceFirst(args2[0] + ":", "");
            if (cm.addSilentClient(args[0], args[1]))
                log.cmd("new >> Added new Client " + args[0] + ":" + args[1]);
            else
                log.cmd("new >> Account \"" + args[0] + "\" already exists");
        } else
            log.error("CMD >> Wrong Format: new [name]:[password]");
    }

    private void delete(String input) {
        String[] args = input.split(" ");
        if (args.length > 1) {
            String extra = input.replace(args[0] + " " + args[1], "");
            cm.deleteClient(args[1], "Account Deleted by Server" + (args.length > 2 ? " - Reason:" + extra : ""));
        } else {
            log.error("CMD >> Wrong Format: delete [name] <reason>");
        }
    }

    private boolean isAlive() {
        try {
            return !serverSocket.isClosed();
        } catch (Exception ignore) {
        }
        return false;
    }

    public ClientManager getClientManager() {
        return cm;
    }

    public Logger getLogger(){
        return log;
    }
}
