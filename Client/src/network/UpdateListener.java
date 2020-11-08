package network;

import Objects.Close;
import Objects.Login;
import Objects.Success;
import Objects.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class UpdateListener extends Thread {

    private Socket server;
    private Client client;
    private ObjectInputStream in;

    boolean running = true;

    public UpdateListener(Socket server, Client client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public synchronized void start() {
        running = true;
        super.start();
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(server.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(running){
            try {
                Object obj = in.readObject();

                if (obj instanceof Text)
                    System.out.println("[" + ((Text) obj).getSender() + "] " + ((Text) obj).getText());
                else if (obj instanceof Success)
                    System.out.println(((Success) obj).getType() + " Request successful with message: " + ((Success) obj).getMsg());
                else if (obj instanceof Close)
                    client.disconnect();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Closing Update Listener");
                running = false;
                client.disconnect();
            }
        }
    }

    public void close(){
        running = false;
        stop();
    }
}
