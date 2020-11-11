package org.luma.client.network;

public class Karl extends ClientMain {

    public Karl(String hostname, int port) {
        super(hostname, port);
    }

    public static void main(String[] args) {
        //new Karl("ask4.ddns.ms", 54321); //Remote Communication
        new Karl("localhost", 54321); //Localhost
    }
}
