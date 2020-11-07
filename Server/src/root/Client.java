package root;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket socket;

    private String name;
    private String password;

    private boolean isLoggedIn = false;

    DataInputStream in;
    DataOutputStream out;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public Client(String name, String password, Socket socket) {
        this.name = name;
        this.password = password;
    }

    public int getCode() throws IOException {
        return in.readInt();
    }

    public void login() throws IOException {
        System.out.println("name: " + read());
        System.out.println("passw: " + read());

        answer(Codes.LOGIN);

        isLoggedIn = true;
    }

    public void text() throws IOException {
        System.out.println(read());
        answer(Codes.TEXT);
    }

    public String read() throws IOException {
        return in.readUTF();
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
        in.close();
        out.close();
        socket.close();
    }
}
