package Objects;

import java.io.Serializable;

public class Success implements Serializable {
    private final String type;
    private final String msg;

    public Success(String type, String msg){
        this.type = type;
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}
