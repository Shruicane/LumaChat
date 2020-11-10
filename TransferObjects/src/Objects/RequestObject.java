package Objects;

import java.io.Serializable;

public class RequestObject implements Serializable {
    private final String type;
    private final String sender;
    private final String message;

    public RequestObject(String type, String sender, String message) {
        this.type = type;
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "RequestObject{" +
                "type='" + type + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
