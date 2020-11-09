package org.luma.server.network;

import Objects.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private MessageListener ml;
    private NetworkListener nl;

    private String name;
    private String password;

    private boolean running = true;

    ObjectInputStream in;
    ObjectOutputStream out;

    Thread inputHandler;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        inputHandler = new Thread(() -> {
            while(running) {
                try {
                    Object obj = in.readObject();

                    if (obj instanceof Text) {
                        ml.shout((Text) obj);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    nl.disconnectClient(this);
                }
            }
        });
    }

    public void setMessageListener(MessageListener ml) {
        this.ml = ml;
    }

    public void setNetworkListener(NetworkListener nl) {
        this.nl = nl;
    }

    public void start() {
        inputHandler.start();
    }

    public void close() {
        try {
            running = false;
            inputHandler.interrupt();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Object obj) {
        try {
            out.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }
}
