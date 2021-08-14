package phesbook.objects;

public class SMessage extends Request {

    private String command;
    private String text;

    public SMessage () {
        super("SMessage");
    }

    public SMessage (String c, String t) {
        super("SMessage");
        command = c;
        text = t;
    }

    public String getText() {
        return text;
    }

    public String getCommand() {
        return command;
    }
}
