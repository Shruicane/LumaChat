package org.luma.client.network;

import Objects.AnswerObject;
import Objects.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOHandler extends Thread {
    private final ClientMain main;
    private boolean running = true;

    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public IOHandler(Socket server, ClientMain main) throws IOException {
        this.main = main;
        out = new ObjectOutputStream(server.getOutputStream());
        in = new ObjectInputStream(server.getInputStream());
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (in) {
                try {
                    Object obj = in.readObject();

                    if (obj instanceof Text) {
                        System.out.println(((Text) obj).getSender() + ": " + ((Text) obj).getMessage());
                    } else if(obj == null) {
                        // Free InputStream from reading in this synchronized Block
                    } else
                        System.out.println(obj.toString());
                } catch (IOException | ClassNotFoundException e) {
                    //main.disconnect("[Client] Server closed");
                }
            }
        }
    }

    public void close() {
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
        interrupt();
    }

    public boolean send(Object obj) {
        try {
            out.writeObject(obj);
            synchronized (in){
                try {
                    AnswerObject answer;
                    while((answer = (AnswerObject) in.readObject()) == null);

                    System.out.println(answer.getType() + " Request was Successful. Server responded with: " +
                            answer.getMessage() + " (" + answer.isSuccess() + ")");

                    return answer.isSuccess();
                } catch (IOException | ClassNotFoundException e) {
                    main.disconnect("[IOHandler:send] Server closed");
                }
            }
        } catch (IOException e) {
            main.disconnect("[IOHandler:send] not connected");
        }
        return false;
    }
}
