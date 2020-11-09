package old_root;

public class Karl extends Client {

    public Karl(String hostname, int port) {
        super(hostname, port, "Karl", "123456");
    }

    public static void main(String[] args) {
        new Karl("localhost", 54321);
    }
}
