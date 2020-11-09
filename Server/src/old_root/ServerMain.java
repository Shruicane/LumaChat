package old_root;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class ServerMain {
    NetworkListener nl;
    MessageListener ml;

    ServerSocket serverSocket;

    boolean running = true;

    public ServerMain(int port) {

        System.out.println("[Server] started");

        start(port);

        while (running) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (input.matches("start")) {
                start(port);
            } else if (input.matches("exit")) {
                exit();
            } else if (input.matches("stop")) {
                stop();
            } else if (input.matches("restart")) {
                stop();
                start(port);
            }

            if (input.split(" ")[0].matches("edit")) {
                edit(input);
            }
        }
    }

    private void edit(String input) {
        String[] cmd = input.split(" ");
        if (cmd.length == 4) {
            Client client = ml.getClient(cmd[1]);
            if (client == null)
                System.out.println("That User does not exist!");
            else if (cmd[2].matches("name")) {
                client.update(cmd[3], null);
                System.out.println("Changed name to ");
            } else if (cmd[2].matches("passw")) {
                client.update(null, cmd[3]);
            } else
                System.out.println("Wrong Format: edit <name> [name]/[passw] <string>");
        } else
            System.out.println("Wrong Format: edit <name> [name]/[passw] <string>");
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(100);
            System.out.println(Utils.getTime() + " [ServerMain] Server started on port " + port);
            running = true;

            ml = new MessageListener();
            ml.start();
            nl = new NetworkListener(serverSocket, ml);
            nl.start();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void exit() {
        System.out.println(Utils.getTime() + " [ServerMain] Stoping Server");
        nl.close();
        try {
            ml.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        System.out.println(Utils.getTime() + " [ServerMain] Shutting down Server");
        running = false;
        exit();
    }

    public static void main(String[] args) {
        new ServerMain(Settings.PORT);
    }
}
