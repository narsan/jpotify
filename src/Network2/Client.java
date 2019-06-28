package Network2;

import Network2.ServerMessagesManager;
import mainPackage.Library;
import mainPackage.Playing;
import mainPackage.SharedPlayList;
import mainPackage.UserPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.ArrayList;
import java.util.Scanner;

//import com.sun.corba.se.impl.ior.WireObjectKeyTemplate;

public class Client {
    Socket mSocket;
    int port = 9093;
    String serverAddress = "localhost";

    InputStream fromServerStream;
    OutputStream toServerStream;

    DataInputStream reader;
    PrintWriter writer;
    static ArrayList<String> friends=new ArrayList<>();
     static SharedPlayList sharedPlayList=null;


    public static ArrayList<String> getFriends() {
        return friends;
    }

    public static void setSharedPlayList(SharedPlayList share){

        sharedPlayList=share;
    }

    public static void addFriends(String name){

        friends.add(name);


    }

    public Client() {
        try {

            JFrame frame=new JFrame();

            String IP=JOptionPane.showInputDialog(frame,"Enter IP");
            serverAddress=IP;

            mSocket = new Socket(serverAddress, port);

            System.out.println("connect to server ....");

            // input stream (stream from server)
            fromServerStream = mSocket.getInputStream();

            // output stream (stream to server)
            toServerStream = mSocket.getOutputStream();

            reader = new DataInputStream(fromServerStream);
            writer = new PrintWriter(toServerStream, true);

            menu();
            // first : read server message
            //String msg = reader.readLine();
            //System.out.println("Server :" + msg);

            // Manage other server message by a Thread
            Thread t = new Thread(new ServerMessagesManager(reader));
            t.start();


        } catch (UnknownHostException e) {
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void menu() {


        //Scanner sc = new Scanner(System.in);

        //String name = sc.nextLine();

        sendName(UserPanel.geName1());
        System.out.println(UserPanel.geName1()+",,,,");

        UserPanel.getShareBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame frame=new JFrame();
                String option= JOptionPane.showInputDialog(frame,"1: to share playlist , 2: to share playing song");
                int n=Integer.parseInt(option);
                if (n==1){

                         for (int i = 0; i <friends.size() ; i++) {
                        for (int j = 0; j < sharedPlayList.getPlayListSongs().size(); j++) {
                            sendFile(friends.get(i),sharedPlayList.getPlayListSongs().get(j).getPath());
                        }
                    }



                }

                else if (n==2){

                    for (int i = 0; i <friends.size() ; i++) {

                        sendSingleCht(friends.get(i), Playing.getTitle());
                    }

                    System.out.println(Playing.getTitle());

                }
            }
        });

       /* while (true) {
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
                    /*Scanner scanner=new Scanner(System.in);
                    System.out.println("Enter receiver name");
                    String to = scanner.nextLine();*/

        //System.out.println("Enter your message");
        //String text=scanner.next();

        for (int i = 0; i <friends.size() ; i++) {

            sendSingleCht(friends.get(i), Playing.getTitle());
        }

        System.out.println(Playing.getTitle());
        // break;
            /*    case 5:
                    System.out.println("Enter your message");
                    text = sc.nextLine();
                    sendGroupCht(text);
                    break;*/
        //case 6:
        Scanner scanner1=new Scanner(System.in);

                  /*  System.out.println("Enter your friend number");
                    int number=scanner1.nextInt();
                    String name1="";*/

                   /* for (int i = 0; i <friends.size() ; i++) {
                        for (int j = 0; j < SharedPlayList.getPlayListSongs().size(); j++) {
                            sendFile(friends.get(i), SharedPlayList.getPlayListSongs().get(j).getPath());
                        }
                    }
                    /*System.out.println("Enter receiver name");
                    to = sc.nextLine();
                    System.out.println("Enter file name(Full Path)");
                    String fileName = sc.nextLine();
                case 7:
                    bye();
                    return;
            }
        }*/

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
        }
    }

    private void bye() {
        writer.println("BYE");
    }

   /* public static void main(String[] args) {
        new Client();
    }*/
}