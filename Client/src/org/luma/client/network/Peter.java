package org.luma.client.network;

public class Peter extends ClientMain {

    public Peter(String hostname, int port) {
        super(hostname, port);
    }

    public static void main(String[] args) {
        new Peter("ask4.ddns.ms", 54321);
    }
}
