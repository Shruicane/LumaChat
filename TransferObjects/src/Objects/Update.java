package Objects;

public class Update extends RequestObject {

    public static final String GROUP = "group";
    public static final String PRIVATE = "private";
    public static final String ONLINE = "online";
    public static final String PRIVATE_CREATE = "privateCreate";
    public static final String PRIVATE_DELETE = "privateDelete";

    public Update(String type, String sender, Object information) {
        super(type, sender, information);
    }
}
