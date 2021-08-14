package phesbook.client;

import com.sun.org.apache.xpath.internal.operations.Bool;
import phesbook.objects.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main (String args []) throws IOException, InterruptedException, ClassNotFoundException {

        // Connecting to socket
        Socket socket = null;
        while (true) {
            try {
                // Tries again and again until connecting
                socket = new Socket("127.0.0.1", 5000);
                System.out.println("Connected!");
                break;
            }
            catch (ConnectException e) {
                // Restarting message
                System.out.println("Start your server and connect again.");
                System.out.println("Retrying in ");
                for (int i=5; i>0; i--) {
                    System.out.print(i + "  ");
                    Thread.sleep(1000);
                }
                System.out.println();
            }
        }

        // Setting up I/O
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

        // Setup console input
        Scanner sc = new Scanner(System.in);

        // Starting input loop
        start: while (true) {
            // Authenticating
            boolean authenticated = false;
            System.out.println("== LMessage ==");
            System.out.println("Enter your username: ");
            String username = sc.nextLine();
            System.out.println("Enter your password: ");
            String passsword = sc.nextLine();
            LMessage lm = new LMessage(username, passsword);
            output.writeObject(lm);

            String confirmation = (String) input.readObject();
            if (confirmation.equals("success")) {
                authenticated = true;
                System.out.println("Successfully authenticated!\n");
            }
            else {
                authenticated = false;
            }

            Thread input_read = new Thread(() -> {
                JFrame f = new JFrame();
                f.setTitle(username);
                f.setSize(200, 200);

                String [][] data = {
                        {"Logged in as " + username, "tahmeed"}
                };
                String [] columnNames = {"Updates"};
                JTable j = new JTable(data, columnNames);
                j.setBounds(0, 0, 400, 400);

                JScrollPane sp = new JScrollPane(j);
                f.add(sp);
                f.setVisible(true);

                DefaultTableModel model = (DefaultTableModel) j.getModel();

                while (true) {
                 try {
                    String line = (String) input.readObject();
                    // model.addRow(new Object[] {line});
                    // j.setModel(model);
                    System.out.println(line);

                 }
                 catch (IOException | ClassNotFoundException e) {
                     break;
                 }
                }
            });
            input_read.start();

            login: while (authenticated) {
                {
                    // Print options
                    System.out.println("Options:");
                    System.out.println("1. BMessage (Broadcast)");
                    System.out.println("2. SMessage (Server)");
                    System.out.println("3. CMessage (Client)");
                    System.out.println("4. Exit");
                }

                int n = sc.nextInt();
                sc.skip("\n");

                if (n == 1) {
                    System.out.println("== BMessage == \n");
                    System.out.println("Text [INPUT]:");
                    String text = sc.nextLine();

                    BMessage bm = new BMessage(text);
                    output.writeObject(bm);
                    System.out.println("Message sent!\n");
                }
                else if (n == 2) {
                    System.out.println("== SMessage == \n");
                    System.out.println("Command [INPUT show: 's' logout: 'l']: ");
                    String command = sc.nextLine();
                    System.out.println("Text [INPUT]:");
                    String text = sc.nextLine();

                    if (command.equals("l")) {
                        SMessage sm = new SMessage("logout", text);
                        output.writeObject(sm);
                        System.out.println("Successfully logged out!\n");
                        break login;
                    }
                    else {
                        SMessage sm = new SMessage("show", text);
                        output.writeObject(sm);
                    }
                }
                else if (n == 3) {
                    System.out.println("== CMessage == \n");
                    SMessage sm = new SMessage("show", "cm");
                    output.writeObject(sm);
                    Thread.sleep(500);
                    System.out.println("Receiver [INPUT]:");
                    String receiver = sc.nextLine();
                    System.out.println("Text [INPUT]:");
                    String text = sc.nextLine();
                    System.out.println("File name [INPUT] [OPTIONAL]:");
                    String file = sc.nextLine();

                    if (file.equals("")) {
                        CMessage cm = new CMessage(receiver, text);
                        output.writeObject(cm);
                        System.out.println("Message sent!\n");
                    }
                    else {
                        CMessage cm = new CMessage(receiver, text, file);
                        output.writeObject(cm);
                        File f = new File("D:\\BUET\\1. Tahmeed\\CSE 108 Lab\\Week 11\\phesbook\\src\\phesbook\\client\\images\\" + file);
                        FileInputStream fis = null;
                        fis = new FileInputStream(f);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        byte[] contents;
                        long fileLength = f.length();
                        output.writeObject(String.valueOf(fileLength));
                        output.flush();
                        long current = 0;

                        while(current!=fileLength){
                            int size = 10000;
                            if(fileLength - current >= size)
                                current += size;
                            else{
                                size = (int)(fileLength - current);
                                current = fileLength;
                            }
                            contents = new byte[size];
                            bis.read(contents, 0, size);
                            output.write(contents);
                        }
                        output.flush();

                        System.out.println("Message and file sent!\n");

                    }
                }
                else {
                    break start;
                }

            }

            input_read.interrupt();

            if (!authenticated)
                System.out.println("Username or password is incorrect. Please try again.");
        }

        // Closing connection
        socket.close();

    }

}
