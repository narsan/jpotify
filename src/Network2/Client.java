package Network2;

import mainPackage.Library;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

//import com.sun.corba.se.impl.ior.WireObjectKeyTemplate;

public class Client {
    Socket mSocket;
    int port = 9093;
    String serverAddress = "192.168.43.236";

    InputStream fromServerStream;
    OutputStream toServerStream;

    DataInputStream reader;
    PrintWriter writer;

    public Client() {
        try {

            mSocket = new Socket(serverAddress, port);

            System.out.println("connect to server ....");

            // input stream (stream from server)
            fromServerStream = mSocket.getInputStream();

            // output stream (stream to server)
            toServerStream = mSocket.getOutputStream();

            reader = new DataInputStream(fromServerStream);
            writer = new PrintWriter(toServerStream, true);

            // first : read server message
            String msg = reader.readLine();
            System.out.println("Server :" + msg);

            // Manage other server message by a Thread
            Thread t = new Thread(new ServerMessagesManager(reader));
            t.start();

            menu();

        } catch (UnknownHostException e) {
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void menu() {

        Scanner sc = new Scanner(System.in);

        String name = sc.nextLine();

        sendName(name);

        while (true) {
            System.out.println("-----------");
            System.out.println("Enter Command number(ex:3):\n"
                    + "6)send file\n"
                    + "7)exit");

            int commandNumber = Integer.parseInt(sc.nextLine());

            switch (commandNumber) {

                case 3:
                    System.out.println("Enter message");
                    String msg = sc.nextLine();

                    echo(msg);

                    break;
                case 4:
                    System.out.println("Enter receiver name");
                    String to = sc.nextLine();

                    System.out.println("Enter your message");
                    String text = sc.nextLine();

                    sendSingleCht(to, text);
                    break;
                case 5:
                    System.out.println("Enter your message");
                    text = sc.nextLine();

                    sendGroupCht(text);
                    break;
                case 6:
                    System.out.println("Enter receiver name");
                    to = sc.nextLine();

                    for (int i = 0; i < Library.getSongs().size(); i++) {

                        sendFile(to, Library.getSongs().get(i).getPath());
                    }

                    // System.out.println("Enter file name(Full Path)");
                    //String fileName = sc.nextLine();

                    break;
                case 7:

                    bye();

                    return;

            }
        }
    }

    private void sendName(String name) {
        writer.println(name);
    }


    private void echo(String msg) {
        writer.println("ECHO");
        writer.println(msg);
    }

    private void sendSingleCht(String to, String text) {
        writer.println("SCHT");
        writer.println(to);
        writer.println(text);
    }

    private void sendGroupCht(String text) {
        writer.println("GCHT");
        writer.println(text);
    }

    private void sendFile(String to, String fileName) {
        File file = new File(fileName);
        long fileLength = file.length();

        writer.println("SFILE");
        writer.println(file.getName());
        writer.println(to);
        writer.println("" + fileLength);

        try {

            // convert file to byte array
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            byte[] fileData = new byte[(int) fileLength];
            dis.readFully(fileData);

            // send byte array to server
            toServerStream.write(fileData);

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println();
        }
    }

    private void bye() {
        writer.println("BYE");
    }

    /*public static void main(String[] args) {

        new Client();
    }*/
}
