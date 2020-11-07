package network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class UpdateListener extends Thread {

    private Socket server;
    private DataInputStream in;

    boolean running = true;

    public UpdateListener(Socket server) throws IOException {
        this.server = server;
        in = new DataInputStream(server.getInputStream());
    }

    @Override
    public void run() {
        while(running){
            try {
                int code = in.readInt();
                System.out.println("Code: " + code);

                switch (code){
                    case 3:
                        System.out.println(in.readUTF());
                        break;
                }
            } catch (IOException e) {
                System.out.println("Closing Update Listener");
            }
        }
    }

    public void close(){
        running = false;
        interrupt();
    }
}
