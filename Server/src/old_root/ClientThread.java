package old_root;

import Objects.Login;
import Objects.Text;

import java.io.IOException;

public class ClientThread extends Thread {
    private Client client;

    public ClientThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while (!client.isClosed()) {
                Object obj = client.read();

                if (obj instanceof Login)
                    client.login((Login) obj);
                else if (obj instanceof Text)
                    client.text((Text) obj);
                else
                    System.out.println(obj.toString());
            }
            client.close();
        } catch (IOException | ClassNotFoundException e) {
            client.removeClient();
            System.out.println(Utils.getTime() + " [ClientThread] Client <" + client.getName() + "> disconnected");
        }
    }
}
