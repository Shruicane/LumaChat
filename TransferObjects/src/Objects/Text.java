package Objects;

import java.io.Serializable;

public class Text implements Serializable {
    private String text;

    public Text(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
