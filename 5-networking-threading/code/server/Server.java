package phesbook.server;

import phesbook.objects.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {

    public static ArrayList<Connection> clients = new ArrayList<>();
    public static ThreadGroup clientGroup = new ThreadGroup("Clients");

    public static void main (String args[]) throws IOException, ClassNotFoundException {

	    // Creating the server
        Socket socket;
        ServerSocket server = new ServerSocket(5000);
        System.out.println("Server Started!");


        // Accepting new clients (socket)
        while (true) {
            socket = server.accept();
            clients.add(new Connection(socket, clientGroup));
        }

    }

    private static void log (String str) {
        System.out.printf("%s [%s]\n", str, "Main thread");
    }

}
