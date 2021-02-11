package org.luma.server.network;

import Objects.Login;
import Objects.RequestObject;
import Objects.Success;
import Objects.GroupText;
import Objects.Get;
import Objects.PrivateText;
import Objects.Register;
import Objects.Update;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class Client {
    private Socket socket;
    private MessageListener ml;
    private NetworkListener nl;
    private Logger log;

    private String name;
    private String password;

    private boolean loggedIn = false;

    private boolean running = true;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Thread ioHandler;

    public Client(Login login) {
        this.name = login.getSender();
        this.password = (String) login.getInformation();
    }

    public Client(Socket socket, Logger log) throws IOException {
        this.socket = socket;
        this.log = log;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        // Handels Connection (In/Out) to Single Client
        ioHandler = new Thread(() -> {
            while (running) {
                try {
                    Object obj = in.readObject();

                    log.info("Recieved Object: " + obj.toString());

                    if (!loggedIn) {
                        if (obj instanceof Login) {
                            if (loggedIn = nl.login((Login) obj, this)) {
                                send(new Success((RequestObject) obj, "Login Successful", true));

                                name = ((Login) obj).getSender();
                                password = (String) ((Login) obj).getInformation();

                                ml.shout(name, "[+] " + name);

                                send(new Update("group", "System", nl.getAllGroupsWithUser(name)));
                                send(new Update("private", "System", nl.getAllChatsFromUser(name)));
                                nl.sendAll(new Update("online", "System", nl.getAllOnlineClients()));
                            } else {
                                send(new Success((RequestObject) obj, "Login Failed", false));
                            }
                        } else if (obj instanceof Register) {
                            boolean register = nl.register((Register) obj, this);
                            if (register) {
                                send(new Success((RequestObject) obj, "Register Successful", true));

                                name = ((Register) obj).getSender();
                                password = (String) ((Register) obj).getInformation();
                            } else {
                                send(new Success((RequestObject) obj, "Name already exists!", false));
                            }
                        } else {
                            send(new Success((RequestObject) obj, "Not Logging in!", false));
                        }
                    } else if (obj instanceof Get) {
                        if (((Get) obj).getType().matches("onlineClients"))
                            send(new Success((Get) obj, ml.getOnlineClients(this), true));
                    } else if (obj instanceof GroupText) {
                        send(new Success((RequestObject) obj, "GroupMessage Received", true));
                        String group = ((GroupText) obj).getType();
                        ml.message(group, name, (String) ((GroupText) obj).getInformation());
                        log.message(name + " -> " + group + " >> " + (String) ((GroupText) obj).getInformation());
                    } else if (obj instanceof PrivateText) {
                        send(new Success((RequestObject) obj, "PrivateMessage Received", true));
                        String receiver = ((PrivateText) obj).getType();
                        ml.messagePrivate(receiver, name, (String) ((PrivateText) obj).getInformation());
                        log.message(name + " -> " + receiver + " >> " + (String) ((PrivateText) obj).getInformation());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    if (!socket.isClosed())
                        nl.disconnectClient(this);
                }
            }
        });
        ioHandler.setDaemon(true);
    }

    public void start() {
        ioHandler.start();
    }

    public void close() {
        try {
            running = false;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Object obj) {
        try {
            log.info("Send Object: " + obj.toString());
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

    public boolean checkName(String name) {
        if (this.name == null)
            return false;
        return this.name.equalsIgnoreCase(name);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMessageListener(MessageListener ml) {
        this.ml = ml;
    }

    public void setNetworkListener(NetworkListener nl) {
        this.nl = nl;
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
