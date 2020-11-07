package root;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class NetworkListener extends Thread {
    private ServerSocket serverSocket;
    private MessageListener ml;

    private boolean running = true;

    public NetworkListener(ServerSocket serverSocket, MessageListener ml) {
        System.out.println(Utils.getTime() + " [NetworkListener] Started Listener");
        this.serverSocket = serverSocket;
        this.ml = ml;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ml.addClient(socket);
                System.out.println(Utils.getTime() + " [NetworkListener] New client connected");
            } catch (IOException e) {
                /* ignore */
            }
        }
    }

    public void close() {
        running = false;
        interrupt();
        System.out.println(Utils.getTime() + " [NetworkListener] Closed NetworkListener");
    }
}
