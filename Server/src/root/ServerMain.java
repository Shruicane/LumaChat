package root;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class ServerMain {
    NetworkListener nl;
    MessageListener ml;

    boolean running = true;

    public ServerMain(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(100);
            System.out.println(Utils.getTime() + " [ServerMain] Server started on port " + port);

            ml = new MessageListener();
            ml.start();
            nl = new NetworkListener(serverSocket, ml);
            nl.start();

            while (running) {
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();

                if(input.matches("stop")){
                    stop();
                }
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void stop() throws IOException {
        System.out.println(Utils.getTime() + "[ServerMain] Shutting down Server");
        running = false;
        nl.close();
        ml.close();

    }

    public static void main(String[] args) {
        new ServerMain(Settings.PORT);
    }
}
