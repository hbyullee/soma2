package com.swmaestro.rankserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class RankServer {

	public static final int THREADPOOL_SIZE = 50 ;
	static HashMap<String, User> userMap  = new  HashMap<String, User>();
	ServerSocket 	serverSocket ;
	ServerThread 	serverThread ;
	Executor 				exec ;
	
	public RankServer() {
		for (int i = 0; i < 10000; i++) {
			userMap.put("id"+Integer.toString(i), new User(null, i, 0, 0));
		}
		try {
			exec = Executors.newFixedThreadPool(THREADPOOL_SIZE);
			serverSocket = new ServerSocket(8988);
		} catch (IOException e) {
			System.out.println("Server Socket 생성");
			e.printStackTrace();
		}
	Collections.synchronizedMap(userMap);
	}
	
	void startServer(){
		try {
			/*
			 * 클라이언트로부터 TCP 연결을 기다립니다. TCP 연결이 되면 Socket객체를 리턴합니다.
			 */
			
			while (true) {
				Socket clientSocket = serverSocket.accept();
				if(clientSocket == null)
					break;
					
				// 서버 쓰레드를 생성하여 실행한다.
				serverThread = new ServerThread(clientSocket);
				exec.execute(serverThread) ;
//				serverThread.start();
			}
			

		} catch (IOException ie) {
			ie.printStackTrace();
		} 
		
	}
	
	public static void main(String []args){
		
		RankServer rankServer = new RankServer();
		rankServer.startServer();
		
	}
	
	
	
	
	
	
}
