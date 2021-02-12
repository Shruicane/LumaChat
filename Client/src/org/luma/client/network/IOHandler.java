package org.luma.client.network;

import Objects.*;
import org.luma.client.frontend.GUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOHandler extends Thread {
    private final ClientMain main;
    private final Logger log;
    private final GUI gui;

    private boolean running = true;

    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public IOHandler(Socket server, ClientMain main, Logger log, GUI gui) throws IOException {
        this.main = main;
        this.log = log;
        this.gui = gui;
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

                    if (obj == null) {
                        // Free InputStream from reading in this synchronized Block
                    } else if (obj instanceof GroupText) {
                        log.message("group", ((GroupText) obj).getType(), ((GroupText) obj).getSender() + ": " + ((GroupText) obj).getInformation());
                    } else if (obj instanceof PrivateText) {
                        log.message("private", ((PrivateText) obj).getType(), ((PrivateText) obj).getType() + ": " + ((PrivateText) obj).getInformation());
                    } else if (obj instanceof SystemText) {
                        log.system("group", ((SystemText) obj).getType(), (String) ((SystemText) obj).getInformation());
                    } else if (obj instanceof WarnText) {
                        gui.showPopup(((WarnText) obj).getType(), (String) ((WarnText) obj).getInformation());
                    } else if (obj instanceof Update) {
                        if(((Update) obj).getType().equals(Update.GROUP))
                            gui.updateGroupView(((Update) obj).getInformation());
                        else if(((Update) obj).getType().equals(Update.PRIVATE))
                            gui.updatePrivateView(((Update) obj).getInformation());
                        else if(((Update) obj).getType().equals(Update.ONLINE))
                            gui.updateOnlineClients(((Update) obj).getInformation());
                    } else
                        log.info(obj.toString());
                } catch (IOException | ClassNotFoundException e) {
                    if (main.isConnected())
                        main.disconnect("Client >> Server closed");
                }
            }
        }
    }

    public void close() {
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        running = false;
        interrupt();
    }

    public boolean send(Object obj) {
        try {
            out.writeObject(obj);
            synchronized (in) {
                try {
                    AnswerObject answer;
                    while ((answer = (AnswerObject) in.readObject()) == null) ;

                    log.info(answer.getType() + " Request was Successful. Server responded with: " +
                            answer.getMessage() + " (" + answer.isSuccess() + ")");

                    return answer.isSuccess();
                } catch (IOException | ClassNotFoundException e) {
                    if (main.isConnected())
                        main.disconnect("IOHandler >> Server closed");
                }
            }
        } catch (IOException e) {
            if (main.isConnected())
                main.disconnect("IOHandler >> Not connected");
        }
        return false;
    }
}
