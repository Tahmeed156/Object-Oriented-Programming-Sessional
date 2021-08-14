package phesbook.objects;

public class LMessage extends Request {

    private String username;
    private String password;
    private String type;

    public LMessage () {
        super("LMessage");
    }

    public LMessage (String u, String p) {
        super("LMessage");
        username = u;
        password = p;
        type = "normal";
    }

    public LMessage (String u, String p, String t) {
        super("LMessage");
        username = u;
        password = p;
        type = t;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }
}
