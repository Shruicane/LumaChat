package org.luma.client.network;

public class Peter extends ClientMain {

    public Peter(String hostname, int port) {
        super("Peter", "654321");
    }

    public static void main(String[] args) {
        new Peter("localhost", 54321);
    }
}
