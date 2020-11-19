package org.luma.server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Scanner;

public class ServerMain {
    private ServerSocket serverSocket;
    private NetworkListener nl;
    private final ClientManager cm;

    private boolean running = true;

    public ServerMain() {
        long startTime = System.currentTimeMillis();
        cm = new ClientManager();
        start();
        Logger.network("Server >> Done! (" + (System.currentTimeMillis() - startTime) + "ms)");

        while (running) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String cmd = input.split(" ")[0];

            if(cmd.startsWith("//")) {
                cmd = cmd.replaceFirst("//", "");
                if (cmd.matches("start")) {
                    start("");
                } else if (cmd.matches("restart")) {
                    restart();
                } else if (cmd.matches("stop")) {
                    stop();
                } else if (cmd.matches("exit")) {
                    close();
                } else if (cmd.matches("help")) {
                    help();
                } else if (cmd.matches("list")) {
                    list(input);
                } else if (cmd.matches("kick")) {
                    kick(input);
                } else if (cmd.matches("edit")) {
                    edit(input);
                } else if (cmd.matches("new")) {
                    newC(input);
                } else if (cmd.matches("delete")) {
                    delete(input);
                } else {
                    Logger.error("CMD >> Unknown Command: \"" + input + "\" try help for more information");
                }
            }
        }
    }

    private void start(String nllptr) {
        if (isAlive())
            Logger.warning("Server >> Already running!");
        else
            start();
    }

    private void start() {
        try {
            running = true;
            Logger.network("Server >> Starting");
            serverSocket = new ServerSocket(54321);
            serverSocket.setSoTimeout(100);
            nl = new NetworkListener(cm, serverSocket);
            nl.start();
        } catch (SocketException e) {
            Logger.error("Server >> Address already in use");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        stop();
        start();
    }

    private void stop() {
        close();
        Logger.network("Server >> Stopping");
        running = false;
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

    private void help() {
        Logger.cmd("help >> start");
        Logger.cmd("help >> restart");
        Logger.cmd("help >> stop");
        Logger.cmd("help >> exit");
        Logger.cmd("help >> list all/connected/online");
        Logger.cmd("help >> kick [name] <reason>");
        Logger.cmd("help >> edit name/passw [name] [string]");
        Logger.cmd("help >> new [name]:[password]");
        Logger.cmd("help >> delete [name] <reason>");
    }

    private void list(String input) {
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
    }

    private void kick(String input) {
        String[] args = input.split(" ");
        if (args.length > 1) {
            String extra = input.replace(args[0] + " " + args[1], "");
            cm.kick(args[1], "Kicked by Server" + (args.length > 2 ? " - Reason:" + extra : ""));
        } else {
            Logger.error("CMD >> Wrong Format: kick [name] <reason>");
        }
    }

    private void edit(String input) {
        String[] cmd = input.split(" ");
        if (cmd.length == 4) {
            Client client = cm.findClientFromAll(cmd[2]);
            if (client == null)
                Logger.error("CMD >> That User does not exist!");
            else if (cmd[1].matches("name")) {
                client.update(cmd[3], null);
                Logger.cmd("edit >> Changed name from \"" + Logger.italic(cmd[2]) + "\" to \"" + Logger.italic(cmd[3]) + "\"");
            } else if (cmd[1].matches("passw")) {
                client.update(null, cmd[3]);
                Logger.cmd("edit >> Changed password from \"" + Logger.italic(cmd[2]) + "\" to \"" + Logger.italic(cmd[3]) + "\"");
            } else
                Logger.error("CMD >> Wrong Format: edit name/passw [name] [string]");
        } else
            Logger.error("CMD >> Wrong Format: edit name/passw [name] [string]");
    }

    private void newC(String input) {
        String[] args = input.split(" ");
        if (args.length == 2 && args[1].contains(":")) {
            args = args[1].split(":");
            args[1] = input.split(" ")[1].replaceFirst(args[0] + ":", "");
            //String[] args2 = args[1].split(":");
            //args2[1] = args[1].replaceFirst(args2[0] + ":", "");
            if (cm.addSilentClient(args[0], args[1]))
                Logger.cmd("new >> Added new Client " + args[0] + ":" + args[1]);
            else
                Logger.cmd("new >> Account \"" + args[0] + "\" already exists");
        } else
            Logger.error("CMD >> Wrong Format: new [name]:[password]");
    }

    private void delete(String input) {
        String[] args = input.split(" ");
        if (args.length > 1) {
            String extra = input.replace(args[0] + " " + args[1], "");
            cm.deleteClient(args[1], "Account Deleted by Server" + (args.length > 2 ? " - Reason:" + extra : ""));
        } else {
            Logger.error("CMD >> Wrong Format: delete [name] <reason>");
        }
    }

    private boolean isAlive() {
        try {
            return !serverSocket.isClosed();
        } catch (Exception ignore) {
        }
        return false;
    }

    public static void main(String[] args) {
        new ServerMain();
    }
}
