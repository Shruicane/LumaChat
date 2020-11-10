package Objects;

import java.io.Serializable;

public class AnswerObject implements Serializable {
    private final RequestObject request;
    private final String type;
    private final String message;
    private final boolean success;

    public AnswerObject(RequestObject request, String message, boolean success){
        this.request = request;
        if(request == null)
            type = "";
        else
            type = request.getClass().getSimpleName();
        this.message = message;
        this.success = success;
    }

    public RequestObject getRequest() {
        return request;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "AnswerObject{" +
                "request=" + request +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
