package old_root;

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

        send(new Success("Login", "Welcome!"));

        ml.message(new Text("System", "[" + name + "] logged in"));
    }

    public void text(Text text) throws IOException {
        System.out.println("[Text]");
        System.out.println("Sender: " + text.getSender());
        System.out.println("Text: " + text.getText());
        send(new Success("Text", text.getText()));

        ml.message(text);
    }

    public Object read() throws ClassNotFoundException, IOException {
        return in.readObject();
    }

    public void send(Object obj) throws IOException {
        out.writeObject(obj);
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

    public void update(String name, String password) {
        if (name != null) this.name = name;
        if (password != null) this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean compareName(String name) {
        return this.name.matches(name);
    }

    public boolean comparePassword(String password){
        return this.password.matches(password);
    }
}
