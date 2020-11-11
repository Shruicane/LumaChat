package org.luma.server.network;

import Objects.Login;
import Objects.RequestObject;
import Objects.Success;
import Objects.Text;
import Objects.SystemText;
import Objects.Get;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class Client {
    private Socket socket;
    private MessageListener ml;
    private NetworkListener nl;

    private String name;
    private String password;

    private boolean loggedIn = false;

    private boolean running = true;

    ObjectInputStream in;
    ObjectOutputStream out;

    Thread inputHandler;

    public Client(Login login) {
        this.name = login.getSender();
        this.password = login.getMessage();
    }

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        inputHandler = new Thread(() -> {
            while (running) {
                try {
                    Object obj = in.readObject();

                    Logger.info("Recieved Object: " + obj.toString());

                    if (!loggedIn) {
                        if (obj instanceof Login) {
                            if (loggedIn = nl.login((Login) obj, this)) {
                                send(new Success((RequestObject) obj, "Login Successful", true));
                                name = ((Login) obj).getSender();
                                password = ((Login) obj).getMessage();
                                ml.shout(new SystemText("[+] " + name));
                                send(new SystemText("Online Users: " + ml.getOnlineClients()));
                            } else {
                                send(new Success((RequestObject) obj, "Login Failed", false));
                            }
                        } else {
                            send(new Success((RequestObject) obj, "Not Logging in!", false));
                        }
                    } else if (obj instanceof Get) {
                        if (((Get) obj).getType().matches("onlineClients"))
                            send(new Success((Get) obj, ml.getOnlineClients(), true));
                    } else {
                        if (obj instanceof Text) {
                            send(new Success((RequestObject) obj, "Message Received", true));
                            ml.shout(new Text(name, ((Text) obj).getMessage()));
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    if (!socket.isClosed())
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
            //inputHandler.interrupt();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Object obj) {
        try {
            Logger.info("Send Object: " + obj.toString());
            // Free Client InputStream from reading
            out.writeObject(null);
            out.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        loggedIn = false;
    }

    public void update(String name, String password) {
        if (name != null) this.name = name;
        if (password != null) this.password = password;
    }

    public String getName() {
        return name;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean checkName(String name) {
        return this.name.equals(name);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(name, client.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
