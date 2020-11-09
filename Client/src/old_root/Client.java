package old_root;

import Objects.Close;
import Objects.Login;
import Objects.Success;
import Objects.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {
    private Socket server;
    private Thread ul;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private boolean running = true;
    private boolean loggedin = false;

    private String name;
    private String password;

    private String host;
    private int port;

    public Client(String host, int port, String name, String password) {
        this.name = name;
        this.password = password;
        this.host = host;
        this.port = port;

        System.out.println("[Client] started");

        while (isAlive() && running) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.matches("exit")) {
                disconnect();
            } else if (input.matches("stop")) {
                stop();
            } else if (input.split(" ")[0].matches("login") && !loggedin) {
                try {
                    String[] acc = input.split(" ")[1].split(":");
                    login(acc[0], acc[1]);
                } catch (Exception e) {
                    login(name, password);
                }
            } else if (!input.isEmpty() && loggedin) {
                send(input);
            }
        }
    }

    private void init() throws IOException {
        server = new Socket();
        try {
            server.connect(new InetSocketAddress(host, port), 1000);
        } catch (IOException e) {
            System.out.println("[Client] Server is not reachable");
        }
        out = new ObjectOutputStream(server.getOutputStream());
        in = new ObjectInputStream(server.getInputStream());

        ul = new Thread(() -> {
            while (loggedin) {
                try {
                    Object obj = in.readObject();

                    if (obj instanceof Text)
                        System.out.println(getTime() + " [" + ((Text) obj).getSender() + "] " + ((Text) obj).getText());
                    else if (obj instanceof Success)
                        System.out.println("[Client] " + ((Success) obj).getType() + " Request successful with message: " + ((Success) obj).getMsg());
                    else if (obj instanceof Close)
                        disconnect();
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("[Client] Closing Listener");
                    disconnect();
                }
            }
        });

        System.out.println("[Client] Connedted to port " + port);
    }

    private boolean isAlive() {
        return true;
    }

    void send(String str) {
        try {
            out.writeObject(new Text(name, str));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(String name, String password) {
        try {
            this.name = name;
            this.password = password;
            init();
            out.writeObject(new Login(name, password));
            if(in.readObject() instanceof Success)
                loggedin = true;
            else
                System.out.println("[Client] Login failed");
            ul.start();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            System.out.println("[Client] Closing Connection");
            loggedin = false;
            //ul.stop(); optional for not having disconnected() called second time on cmd 'exit'
            server.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        disconnect();
        System.exit(-1);
    }

    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return formatter.format(date);
    }
}
