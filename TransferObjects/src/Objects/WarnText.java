package Objects;

public class WarnText extends RequestObject{
    public WarnText (String type, String reason) {
        super(type, "System", reason);
    }
}
