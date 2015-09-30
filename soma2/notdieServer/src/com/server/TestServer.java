package com.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.activation.MimetypesFileTypeMap;

/**
 * 클라이언트에게 자신의 이름을 알려주는 서버입니다. 8988 TCP 포트를 Listen합니다. 클라이언트가 TCP 연결을 하면 자신의 이름을
 * 클라이언트에게 보냅니다.
 */
public class TestServer {

	public static void main(String[] args) {

		ServerSocket 	serverSocket = null;
		Socket 			clientSocket = null;
		ServerThread 	serverThread = null;
		
		int 		 THREADPOOL_SIZE = 50 ;
		Executor 				exec = Executors.newFixedThreadPool(THREADPOOL_SIZE) ;
		
		try {
			serverSocket = new ServerSocket(8988);
			System.out.println("Server Socket 생성");
			/*
			 * 클라이언트로부터 TCP 연결을 기다립니다. TCP 연결이 되면 Socket객체를 리턴합니다.
			 */
			
			while (true) {
				Socket clientSocket2 = serverSocket.accept();
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
	

}

