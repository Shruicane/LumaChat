package org.luma.server.network;

public class MessageListener {
    private final ClientManager cm;
    private Logger log;

    public MessageListener(ClientManager cm, Logger log) {
        log.network("MessageListener >> Started Listener");
        this.cm = cm;
        this.log = log;
    }

    public void message(String group, String sender, String message) {
        cm.message(group, sender, message);
    }

    public void messagePrivate(String receiver, String sender, String message) {
        cm.messagePrivate(receiver, sender, message);
    }

    public void shout(String sender, String message) {
        cm.shout(sender, message);
    }

    public String getOnlineClients(Client client){
        String name = client.getName();
        client.setName(name + " (You)");
        String output =  cm.formatList(cm.getOnlineClients());
        client.setName(name);
        return output;
    }
}
