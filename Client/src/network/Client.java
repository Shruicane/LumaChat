package network;

import Objects.Login;
import Objects.Text;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket server;
    private UpdateListener ul;

    private ObjectOutputStream out;

    private boolean running = true;

    private String name;
    private String password;

    private String host;
    private int port;

    public Client(String host, int port, String name, String password) {
        this.name = name;
        this.password = password;
        this.host = host;
        this.port = port;

        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Connecting to port " + port);
        login();

        Thread cmds = new Thread(() -> {
            while (running) {
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (input.matches("exit")) {
                    disconnect();
                } else if (input.matches("stop")) {
                    stop();
                } else if (input.matches("login")) {
                    try {
                        init();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    login();
                } else {
                    send(input);
                }
            }
        });

        cmds.start();

        while (isAlive() && running) {

        }
        disconnect();
    }

    private void init() throws IOException {
        server = new Socket();
        server.connect(new InetSocketAddress(host, port), 1000);
        out = new ObjectOutputStream(server.getOutputStream());

        ul = new UpdateListener(server, this);
        ul.start();
    }

    private boolean isAlive() {
        return true;
    }

    private void send(String str) {
        try {
            Text textOut = new Text(name, str);
            out.writeObject(textOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login() {
        try {
            Login loginOut = new Login(name, password);
            out.writeObject(loginOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            System.out.println("Closing Connection");
            server.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        disconnect();
        System.exit(-1);
    }
}
