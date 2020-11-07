package root;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class MessageListener extends Thread {

    private LinkedList<Client> clients = new LinkedList<>();

    public MessageListener(){
        System.out.println(Utils.getTime() + " [MessageListener] Started Listener");
    }

    public void message(Client sender, String message) throws IOException {
        for(Client client:clients) {
            client.send(message);
        }
    }

    public void addClient(Socket socket) throws IOException {
        Client client = new Client(socket, this);
        clients.add(client);
        new ClientThread(client).start();
    }

    public void close() throws IOException {
        this.interrupt();
        for (Client client : clients) {
            client.close();
        }
        System.out.println(Utils.getTime() + " [MessageListener] Closed MessageListener");
    }
}
