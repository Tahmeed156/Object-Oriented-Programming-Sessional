package phesbook.server;

import phesbook.objects.*;
import phesbook.server.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

@SuppressWarnings("Duplicates")
public class Connection extends Thread {

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private String access;

    Connection (Socket soc, ThreadGroup tg) {
        super(tg, "Anonymous");
        try {

            this.socket = soc;

            this.input = new ObjectInputStream(soc.getInputStream());
            this.output = new ObjectOutputStream(soc.getOutputStream());

        }
        catch (IOException e) {
            log("Error in i/o at startup");
        }
        this.start();
    }

    @Override
    public void run() {

        while (true) {

            // Getting message type (up casting)
            Request request;
            try {
                request = (Request) input.readObject();
            }
            catch (IOException | ClassNotFoundException e) {
                System.out.println("User disconnected");
                break;
            }

            String message = request.getMessage();
            System.out.println("The message type: " + message);


            cases: switch (message) {

                case "LMessage": {
                    LMessage lm = (LMessage) request;

                    try {

                        // Taking input from file and storing in arraylist
                        BufferedReader f = new BufferedReader(new FileReader("D:\\BUET\\1. Tahmeed\\CSE 107\\Practice Code\\JAVA\\src\\phesbook\\users.txt"));
                        ArrayList<User> users = new ArrayList<>();
                        while(true) {
                            String line = f.readLine();
                            if (line == null)
                                break;
                            users.add(new User(line.split(",")));
                        }

                        // Verifying if user exists
                        for (User u: users) {
                            if (u.username.equals(lm.getUsername()) && u.password.equals(lm.getPassword())) {
                                send("success");
                                login(u.username, u.type);
                                break cases;
                            }
                        }
                        this.send("failure");
                        break;
                    } catch (IOException e) {
                        System.out.println("Cannot read from USERS file");
                        break;
                    }
                }

                case "BMessage": {
                    BMessage bm = (BMessage) request;
                    if (access.equals("admin")) {
                        for (Connection c: Server.clients) {
                            if (c.getName().equals(getName()))
                                continue;
                            String line = getName() + ": " + bm.getText();
                            c.send(line);
                        }
                    }
                    else {
                        send("You do not have admin privileges");
                    }
                    break;
                }

                case "SMessage": {
                    SMessage sm = (SMessage) request;
                    System.out.println("The command [" + sm.getCommand() + "] says: " + sm.getText());
                    if (sm.getCommand().equals("logout")) {
                        System.out.println(getName() + " left");
                        interrupt();
                    }
                    else {
                        String line = "Active users: [" + Server.clientGroup.activeCount() + "]";
                        // ArrayList<String> users = new ArrayList<>();
                        for (Connection c: Server.clients) {
                            if (c.isAlive()) {
                                line = line.concat(" " + c.getName());
                            }
                        }
                        try {
                            output.writeObject(line);
                        }
                        catch (IOException e) {
                            System.out.println("Could not show users to " + getName());
                        }
                    }
                    break;
                }

                case "CMessage": {
                    CMessage cm = (CMessage) request;
                    for (Connection c: Server.clients) {
                        if (c.getName().equals(cm.getReceiver())) {
                            c.send(getName() + ": " + cm.getText());
                            if (!cm.getSendFile().equals("") || !cm.getSendFile().equals("none")) {
                                c.send("New file: D:\\BUET\\1. Tahmeed\\CSE 108 Lab\\Week 11\\phesbook\\src\\phesbook\\client\\images\\sent\\" + cm.getSendFile());
                            }
                        }
                    }
                    if (cm.getSendFile().equals("") || cm.getSendFile().equals("none")) {
                        break;
                    }
                    else try {
                        String strRecv = (String) input.readObject();
                        int filesize = Integer.parseInt(strRecv);
                        byte[] contents = new byte[10000];

                        FileOutputStream fos = new FileOutputStream("D:\\BUET\\1. Tahmeed\\CSE 108 Lab\\Week 11\\phesbook\\src\\phesbook\\client\\images\\sent\\" + cm.getSendFile());
                        BufferedOutputStream bos = new BufferedOutputStream(fos);

                        int bytesRead = 0;
                        int total = 0;            //how many bytes read

                        while (total != filesize)    //loop is continued until received byte=totalfilesize
                        {
                            bytesRead = input.read(contents);
                            total += bytesRead;
                            bos.write(contents, 0, bytesRead);
                        }
                        bos.flush();
                        System.out.println("File receiving SUCCESSFUL");
                    }
                    catch (IOException | ClassNotFoundException e) {
                        System.out.println("File receiving unsuccessful");
                    }
                    finally {
                        break;
                    }

                }

                default: {
                    log("Type of message not defined");
                }

            }

        }

        try {
            this.input.close();
            this.output.close();
            this.socket.close();
        }
        catch (IOException e) {
            log(e.getMessage());
        }

    }


    // Logging in users (showing status, setting thread name)
    private void login (String name, String type) {
        setName(name);
        access = type;
        System.out.println(getName() + " connected to the network");
    }


    // Sending messages from server to client
    private void send(String str) {
        try {
            output.writeObject(str);
        }
        catch (IOException e) {
            log("Cannot output to client");
        }
    }

    private static class User {
        String username;
        String password;
        String type;

        User (String [] str) {
            username = str[0];
            password = str[1];
            type = str[2];
        }
    }

    private void log (String str) {
        System.out.printf("%s [%s]\n", str, getName());
    }
}