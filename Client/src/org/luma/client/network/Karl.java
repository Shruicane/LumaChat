package org.luma.client.network;

public class Karl extends ClientMain {

    public Karl(String hostname, int port) {
        super(hostname, port, "Karl", "123456");
    }

    public static void main(String[] args) {
        new Karl("ask4.ddns.net", 54321);
    }
}
