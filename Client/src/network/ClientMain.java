package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientMain {
    private Socket server;

    private DataOutputStream out;
    DataInputStream in;

    public ClientMain(String hostname, int port) {

        try {
            server = new Socket(hostname, port);

            out = new DataOutputStream(server.getOutputStream());
            in = new DataInputStream(server.getInputStream());

            System.out.println("Connecting to port " + port);
            login();
            send("Hallo, wie gehts?");
            server.close();
            disconnect();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void send(String str) {
        try {
            out.writeInt(2);
            out.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login() {
        try {
            int code = 1;
            out.writeInt(code);

            out.writeUTF("Peter");
            out.writeUTF("12345");
            //int serverResponse = in.readInt();

            //System.out.println(serverResponse == code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            server.close();
            out.close();
            in.close();
            System.out.println("Closing Connection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClientMain("localhost", 1234);
    }
}
