package org.luma.client.network;

public class Peter extends ClientMain {

    public Peter(String hostname, int port) {
        super(hostname, port, "Peter", "654321");
    }

    public static void main(String[] args) {
        new Peter("ask4.ddns.net", 54321);
    }
}
