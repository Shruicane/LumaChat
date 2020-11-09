package org.luma.client.network;

public class Karl extends ClientMain {

    public Karl(String hostname, int port) {
        super("Karl", "123456");
    }

    public static void main(String[] args) {
        new Karl("localhost", 54321);
    }
}
