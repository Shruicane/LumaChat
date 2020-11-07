package root;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {
    private Client client;

    public ServerThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while (!client.isClosed()) {
                int code = client.getCode();

                switch (code){
                    case Codes.LOGIN:
                        client.login();
                    case Codes.TEXT:
                        client.text();
                }

            }
            client.close();
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }
    }
}
