package network;

import Objects.Login;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientMain2 {
    private Socket server;

    private ObjectOutputStream out;

    boolean running = true;

    public ClientMain2(String hostname, int port) {

        try {
            server = new Socket();
            server.connect(new InetSocketAddress(hostname,port),1000);

            UpdateListener ul = new UpdateListener(server);
            ul.start();

            out = new ObjectOutputStream(server.getOutputStream());

            System.out.println("Connecting to port " + port);
            login();
            send("Hallo, wie gehts?");

            while(server.isConnected());
            disconnect();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void send(String str) {
        try {
            out.writeInt(2);
            out.writeUTF(str);

            //System.out.println("Validation: " + (in.readInt() == 2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login() {
        try {
            out.writeObject(new Login("Hans", "654321"));

            //System.out.println("Validation: " + (in.readInt() == 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            System.out.println("Closing Connection");
            server.close();
            out.close();
            //in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClientMain2("localhost", 54321);
    }

    boolean isAlive(String SERVER_ADDRESS, int TCP_SERVER_PORT){
        try {
            server.connect(new InetSocketAddress(SERVER_ADDRESS,TCP_SERVER_PORT),0);
            return true;
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        return false;
    }
}
