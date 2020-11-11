package org.luma.server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class ServerMain {
    private ServerSocket serverSocket;
    private NetworkListener nl;
    private ClientManager cm;

    private boolean running = true;

    public ServerMain() {

        long startTime = System.currentTimeMillis();

        cm = new ClientManager();
        start();

        Logger.network("Server >> Done! (" + (System.currentTimeMillis() - startTime) + "ms)");
        while (running) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (input.matches("stop")) {
                stop();
            } else if (input.matches("exit")) {
                close();
            } else if (input.matches("start")) {
                if (isAlive())
                    Logger.warning("Server >> Already running!");
                else
                    start();
            } else if (input.split(" ")[0].matches("list")) {
                String[] args = input.split(" ");
                if (args.length > 1 && args[1].matches("all")) {
                    Logger.cmd("list all >> " + cm.getAllClients().size() + " User registered:");
                    Logger.cmd("list all >> " + cm.formatList(cm.getAllClients()));
                } else if (args.length > 1 && args[1].matches("connected")) {
                    Logger.cmd("list connected >> " + cm.getConnectedClients().size() + " User connected:");
                    Logger.cmd("list connected >> " + cm.formatList(cm.getConnectedClients()));
                } else if (args.length > 1 && args[1].matches("online")) {
                    Logger.cmd("list online >> " + cm.getOnlineClients().size() + " User online:");
                    Logger.cmd("list online >> " + cm.formatList(cm.getOnlineClients()));
                } else {
                    Logger.error("CMD >> Wrong Format: list all/connected/online");
                }
            } else if (input.split(" ")[0].matches("kick")) {
                String[] args = input.split(" ");
                if (args.length > 1) {
                    kick(args[1], args.length == 3 ? args[2] : null);
                } else {
                    Logger.error("CMD >> Wrong Format: kick [name] <reason>");
                }
            }
            if (input.split(" ")[0].matches("edit")) {
                edit(input);
            } else if (input.matches("help")) {
                Logger.cmd("help >> start");
                Logger.cmd("help >> stop");
                Logger.cmd("help >> exit");
                Logger.cmd("help >> list all/connected/online");
                Logger.cmd("help >> kick [name] <reason>");
            }
        }
    }

    private void kick(String name, String msg) {
        cm.kick(name, msg);
    }

    private void edit(String input) {
        String[] cmd = input.split(" ");
        if (cmd.length == 4) {
            Client client = cm.findClient(cmd[1]);
            if (client == null)
                Logger.error("CMD >> That User does not exist!");
            else if (cmd[2].matches("name")) {
                client.update(cmd[3], null);
                Logger.cmd("edit >> Changed name from \"" + Logger.italic(cmd[1]) + "\" to \"" + Logger.italic(cmd[3]) + "\"");
            } else if (cmd[2].matches("passw")) {
                client.update(null, cmd[3]);
                Logger.cmd("edit >> Changed password from \"" + Logger.italic(cmd[1]) + "\" to \"" + Logger.italic(cmd[3]) + "\"");
            } else
                Logger.error("CMD >> Wrong Format: edit [name] name/passw [string]");
        } else
            Logger.error("CMD >> Wrong Format: edit [name] name/passw [string]");
    }

    private void start() {
        try {
            Logger.network(Logger.bold("Server >> Starting"));
            serverSocket = new ServerSocket(54321);
            serverSocket.setSoTimeout(100);
            nl = new NetworkListener(cm, serverSocket);
            nl.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        if (isAlive()) {
            try {
                Logger.network("Server >> Closing");
                serverSocket.close();
                cm.close();
                nl.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            Logger.warning("Server >> Already closed");
    }

    private void stop() {
        close();
        Logger.network("Server >> Stopping");
        running = false;
    }

    private boolean isAlive() {
        return !serverSocket.isClosed();
    }

    public static void main(String[] args) {
        new ServerMain();
    }
}
