package Objects;

import java.io.Serializable;

public class RequestObject implements Serializable {
    private final String type;
    private final String sender;
    private final Object information;

    public RequestObject(String type, String sender, Object information) {
        this.type = type;
        this.sender = sender;
        this.information = information;
    }

    public String getSender() {
        return sender;
    }

    public Object getInformation() {
        return information;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "RequestObject{" +
                "type='" + type + '\'' +
                ", sender='" + sender + '\'' +
                ", information='" + information + '\'' +
                '}';
    }
}
