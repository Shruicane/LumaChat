package root;

import Objects.Login;
import Objects.Success;
import Objects.Text;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private MessageListener ml;

    private String name;
    private String password;

    private boolean isLoggedIn = false;

    ObjectInputStream in;
    ObjectOutputStream out;

    public Client(Socket socket, MessageListener ml) throws IOException {
        this.socket = socket;
        this.ml = ml;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void login(Login login) throws IOException {
        name = login.getName();
        password = login.getPassword();
        System.out.println("[Login] " + name + ":" + password);

        send(new Success("Login","Welcome!"));

        ml.message(new Text("System", "[" + name + "] logged in"));

        isLoggedIn = true;
    }

    public void text(Text text) throws IOException {
        System.out.println("[Text]");
        System.out.println("Text: " + text.getText());
        send(new Success("Text",text.getText()));
        ml.message(text);
    }

    public Object read() throws ClassNotFoundException, IOException {
        return in.readObject();
    }

    public void send(Object obj) throws IOException {
        out.writeObject(obj);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public DataInputStream getDataInputStream() throws IOException {
        return new DataInputStream(socket.getInputStream());
    }

    public DataOutputStream getDataOutputStream() throws IOException {
        return new DataOutputStream(socket.getOutputStream());
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }

    public void removeClient() {
        ml.removeClient(this);
        try {
            ml.message(new Text("System", "[" + name + "] disconnected"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }
}
