package phesbook.objects;

public class CMessage extends Request {

    private String receiver;
    private String text;
    private String sendFile;

    public CMessage () {
        super("CMessage");
    }

    public CMessage (String r, String t) {
        super("CMessage");
        text = t;
        receiver = r;
    }

    public CMessage (String r, String t, String f) {
        super("CMessage");
        text = t;
        receiver = r;
        sendFile = f;
    }

    public String getText() {
        return text;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSendFile() {
        return sendFile;
    }
}
