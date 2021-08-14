package phesbook.objects;

import java.io.Serializable;

public class Request implements Serializable {

    private static long serialVersionUID = 1L;
    private String message;

    public Request(String m) {
        message = m;
    }

    public String getMessage() {
        return message;
    }
}
