package com.swmaestro.rankclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RankClient {
    private String msg;
    private Socket socket;
    private String serverIp = "127.0.0.1";
 
    public static void main(String[] args) {
        new RankClient().start();
    }
 
    public void start() {
        try {
            socket = new Socket(serverIp, 8988);
            System.out.println("서버와 연결되었습니다.");
            OutputStream ops = socket.getOutputStream();
            
            
            OutputStreamWriter opsw = new OutputStreamWriter(ops);
            PrintWriter pw = new PrintWriter(opsw);
            
            while(true){
            pw.println("GET /login?id=id10");
            pw.flush();
            
            }
//            opsw.write("GET /login?id=id10");
//            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//            out.writeUTF("GET /login?id=id10");
//            opsw.flush();
//            socket.notifyAll();
//            msg = new Scanner(System.in).nextLine();
            
//            ClientReceiver clientReceiver = new ClientReceiver(socket);
//            ClientSender clientSender = new ClientSender(socket);
//             
//            clientReceiver.start();
//            clientSender.start();
        } catch (IOException e) {
        }
    }
 
    class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream input;
 
        public ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
            }
        }
 
        @Override
        public void run() {
            while (input != null) {
                try {
                    System.out.println(input.readUTF());
                } catch (IOException e) {
                }
            }
        }
    }
 
    class ClientSender extends Thread {
        Socket socket;
        DataOutputStream output;
 
        public ClientSender(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(this.socket.getOutputStream());
            } catch (Exception e) {
            }
        }
 
        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);
            String msg = "";
 
            while (output != null) {
                    msg = sc.nextLine();
                    System.out.println(msg);
                    try {
						output.writeBytes(msg);
					} catch (IOException e) {
						e.printStackTrace();
					}
                    
                    if(msg.equals("exit"))
                        System.exit(0);
                     
//                    output.writeUTF("[" + name + "]" + msg);
            }
        }
    }
}
