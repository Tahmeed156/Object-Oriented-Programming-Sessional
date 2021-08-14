package phesbook.objects;

import java.io.Serializable;

public class BMessage extends Request {

    private String text;

    public BMessage () {
        super("BMessage");
    }

    public BMessage(String t) {
        super("BMessage");
        text = t;
    }

    public String getText() {
        return text;
    }

}
