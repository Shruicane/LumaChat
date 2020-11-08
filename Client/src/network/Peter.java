package network;

public class Peter extends Client {

    public Peter(String hostname, int port) {
        super(hostname, port, "Peter", "654321");
    }

    public static void main(String[] args) {
        new Peter("localhost", 54321);
    }
}
