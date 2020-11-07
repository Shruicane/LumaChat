package root;

import Objects.Login;
import Objects.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private MessageListener ml;

    private String name;
    private String password;

    private boolean isLoggedIn = false;

    ObjectInputStream in;
    DataOutputStream out;

    public Client(Socket socket, MessageListener ml) throws IOException {
        this.socket = socket;
        this.ml = ml;
        in = new ObjectInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public Client(String name, String password, Socket socket) {
        this.name = name;
        this.password = password;
    }

    public int getCode() throws IOException {
        return in.readInt();
    }

    public void login(Login login) throws IOException, ClassNotFoundException {
        System.out.println("[Login]");
        System.out.println("name: " + login.getName());
        System.out.println("passw: " + login.getPassword());

        answer(Codes.LOGIN);

        isLoggedIn = true;
    }

    public void text(Text text) throws IOException, ClassNotFoundException {
        System.out.println("[Text]");
        System.out.println("Text: " + text.getText());
        answer(Codes.TEXT);
        ml.message(this, text.getText());
    }

    public Object read() throws ClassNotFoundException, IOException {
        return in.readObject();
    }

    public void send(String str) throws IOException {
        out.writeInt(3);
        out.writeUTF(str);
    }

    public void answer(int code) throws IOException {
        out.writeInt(code);
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
        //out.close();
        //in.close();
        socket.close();
    }
}
