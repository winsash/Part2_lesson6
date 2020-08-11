package ru.geekbrains.part2.lesson6;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;

    public Client() {
        open();
    }

    private void open() {
        try {
            socket = new Socket("127.0.0.1", 8888);
            System.out.println(socket);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
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
                            closeConnection();
                            break;
                        }
                        System.out.println("\n Message from server: " + in_message);
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
                        closeConnection();
                        break;
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
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

