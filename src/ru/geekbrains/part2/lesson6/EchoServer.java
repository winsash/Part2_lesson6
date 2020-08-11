package ru.geekbrains.part2.lesson6;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    private DataInputStream in;
    private DataOutputStream out;
    public EchoServer() {
        open();
    }

    private void open(){
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Server is running on 8888");

            Socket client = serverSocket.accept();
            System.out.println(client);
            System.out.println(String.format("Client connected: %s", client.getLocalSocketAddress()));

            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            startInboundThread();
            startOutboundThread();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void startInboundThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String in_message = in.readUTF();
                        if (in_message.equalsIgnoreCase("/end")) {
                            break;
                        }
                        System.out.println("\nMessage from client: " + in_message);
                    } catch (IOException e) {
                        closeConnection();
                        break;
                    }
                }
            }
        })
                .start();
    }
    private void startOutboundThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Scanner in = new Scanner(System.in);
                        System.out.print("Enter the message: ");
                        String out_message = in.nextLine();
                        out.writeUTF(out_message);
                        if (out_message.equalsIgnoreCase("/end")) {
                            break;
                        }
                        out_message = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        })
                .start();
    }
    public void closeConnection() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
