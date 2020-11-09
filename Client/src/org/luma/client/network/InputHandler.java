package org.luma.client.network;

import Objects.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class InputHandler extends Thread {
    private Socket server;
    private final ClientMain main;

    ObjectOutputStream out;
    ObjectInputStream in;

    private boolean running = true;

    public InputHandler(Socket server, ClientMain main) throws IOException {
        this.server = server;
        this.main = main;
        out = new ObjectOutputStream(server.getOutputStream());
        in = new ObjectInputStream(server.getInputStream());
    }

    @Override
    public void run() {
        while (running) {
            try {
                Object obj = in.readObject();

                if (obj instanceof Text) {
                    System.out.println(((Text) obj).getSender() + ": " + ((Text) obj).getText());
                }
            } catch (IOException | ClassNotFoundException e) {
                close();
            }
        }
    }

    public void close(){
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
        interrupt();
    }

    public void send(Object obj) {
        try {
            out.writeObject(obj);
        } catch (IOException e) {
            System.out.println("[org.luma.network.InputHandler] not connected");
        }
    }
}
