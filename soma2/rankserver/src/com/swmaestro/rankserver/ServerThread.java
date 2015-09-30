package com.swmaestro.rankserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerThread extends Thread {
	// 파일 요청이 없을 경우의 기본 파일
	private static final String DEFAULT_FILE_PATH = "index.html";
	String id;
	// 클라이언트와의 접속 소켓
	private Socket clientSocket;
	
	DataOutputStream outToClient;
	BufferedReader inFromClient = null;
	
	/**
	 * <pre>
	 * * 기본 생성자
	 * </pre>
	 * 
	 * @param connectionSocket
	 *            클라이언트와의 통신을 위한 소켓
	 */
	
	public ServerThread(Socket connectionSocket) {
		this.clientSocket = connectionSocket;
		try {
			outToClient = new DataOutputStream(this.clientSocket.getOutputStream());
			inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	void getMyRank(DataOutputStream outToClient){
		try {
			if(id==null){
				outToClient.writeBytes("login plz \r\n");
			}else{
				ArrayList<String> arrayList = Util.sortByValue(RankServer.userMap);
				for (int i = 0; i < arrayList.size(); i++) {
					if(arrayList.get(i).equals(id)){
						outToClient.writeBytes("your Ranking : " + Integer.toString(i) + "\r\n");
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void getTopRank(String apiString, DataOutputStream outToClient){
		try {
			String params2[] = apiString.split("&"); 
			
			for (int i = 0; i < params2.length; i++) {
				String keyValue[] = params2[i].split("=");
				
				if(keyValue[0].equals("n")){
					int n = Integer.parseInt(keyValue[1]);
					ArrayList<String> arrayList = Util.sortByValue(RankServer.userMap);
					for (int j = 0; j < n; j++) {
						outToClient.writeBytes(arrayList.get(j)+"\r\n");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void setMyScore(String apiString, DataOutputStream outToClient){
		try {
			String params2[] = apiString.split("&"); 
			
			for (int i = 0; i < params2.length; i++) {
				String keyValue[] = params2[i].split("=");
				
				if(keyValue[0].equals("score")){
					RankServer.userMap.get(id).score = Integer.parseInt(keyValue[1]);
					outToClient.writeBytes("SCORE of " + id+" : " + Integer.toString(RankServer.userMap.get(id).score) +"\r\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void login(String apiString, DataOutputStream outToClient){
		try {
			String params2[] = apiString.split("&"); 
			
			for (int i = 0; i < params2.length; i++) {
				String keyValue[] = params2[i].split("=");
				
				if(keyValue[0].equals("id")){
					if(RankServer.userMap.containsKey(keyValue[1])){
						User user = RankServer.userMap.get(keyValue[1]);
						if(user.clientSocket != null){
							user.clientSocket.close();
						}
						id = keyValue[1];
						user.setClientSocket(clientSocket);
						outToClient.writeBytes("hello " + keyValue[1]+"!! \r\n");
						
					}else{
						outToClient.writeBytes(keyValue[1]+"is not existed");
//						close();
					}
				}
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void run() {
		System.out.println("Thread 실행");

        while(inFromClient != null){
			try {
				// 클라이언트와 통신을 위한 입/출력 2개의 스트림을 생성한다.
	
				// 클라이언트로의 메시지중 첫번째 줄을 읽어들인다.
				String requestMessageLine = inFromClient.readLine();
				System.out.println(requestMessageLine);
				
				// 파싱을 위한 토큰을 생성한다. GET /index.html HTTP/1.0
	//			StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);
				String arrParam[] = requestMessageLine.split(" ");
				
				if(!"GET".equals(arrParam[0]))
				{
					System.out.println("Bad Request");
	
					outToClient.writeBytes("HTTP/1.0 400 Bad Request Message \r\n");
					outToClient.writeBytes("Connection: close\r\n");
					outToClient.writeBytes("\r\n");
					close();
					return;
				}
				
				String apiString = arrParam[1];
				System.out.println(apiString);
				if(apiString.indexOf("/login")!=-1){
					String params[] = apiString.split("\\?");
					login(params[1], outToClient);
					
				}else if(apiString.indexOf("/getMyRank")!=-1){
					getMyRank(outToClient);
				}else if(apiString.indexOf("/getTopRank")!=-1){
					String params[] = apiString.split("\\?");
					getTopRank(params[1], outToClient);
				}else if(apiString.indexOf("/setMyScore")!=-1){
					String params[] = apiString.split("\\?");
					setMyScore(params[1], outToClient);
				}
				
	//
	//			String fileName = arrParam[1];
	//			if(fileName.equals("/kweather"))
	//			{
	//				System.out.println("kweather");
	//				URL url = new URL("http://api.kweather.co.kr/apiguide/forecast_01.html");  // 도시
	//				InputStream is = url.openStream();
	//				
	//				ByteArrayOutputStream baos = new ByteArrayOutputStream(40960);
	//				byte tmpData[] = new byte[10240];
	//				while(true) {
	//					int nRead = is.read(tmpData);
	//					if(nRead == -1)
	//						break;
	//					baos.write(tmpData, 0, nRead);
	//				}
	//				baos.close();
	//				outToClient.write(baos.toByteArray());
	//				outToClient.flush();
	//
	//			}else{
	//			
	//			// 기본적으로 루트(/)로부터 주소가 시작하므로 제거한다.
	//				if (fileName.startsWith("/") == true) {
	//				
	//					if (fileName.length() > 1) {
	//						fileName = fileName.substring(1);
	//					}
	//					// 파일명을 따로 입력하지 않았을 경우 기본 파일을 출력한다.
	//					else {
	//						fileName = DEFAULT_FILE_PATH;
	//					}
	//				}
	//
	//				File file = new File(fileName);
	//				// 요청한 파일이 존재하는가?
	//				if (file.exists()) {
	//					// 존재하는 파일의 MIME타입을 분석한다.
	//					String mimeType = new MimetypesFileTypeMap()
	//							.getContentType(file);
	//
	//					// 파일의 바이트수를 찾아온다.
	//					int numOfBytes = (int) file.length();
	//
	//					// 파일 스트림을 읽어들일 준비를 한다.
	//					FileInputStream inFile 	= new FileInputStream(fileName);
	//					byte[] fileInBytes 		= new byte[numOfBytes];
	//					inFile.read(fileInBytes);
	//
	//					// 정상적으로 처리가 되었음을 나타내는 200 코드를 출력한다.
	//					outToClient.writeBytes("HTTP/1.0 200 Document Follows \r\n");
	//					outToClient.writeBytes("Content-Type: " + mimeType + "\r\n");
	//					// 출력할 컨텐츠의 길이를 출력
	//					outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
	//					outToClient.writeBytes("\r\n");
	//
	//					// 요청 파일을 출력한다.
	//					outToClient.write(fileInBytes, 0, numOfBytes);
	//				} else {
	//					// 파일이 존재하지 않는다는 에러인 404 에러를 출력하고 접속을 종료한다.
	//					System.out.println("Requested File Not Found : " + fileName);
	//
	//					outToClient.writeBytes("HTTP/1.0 404 Not Found \r\n");
	//					outToClient.writeBytes("Connection: close\r\n");
	//					outToClient.writeBytes("\r\n");
	//				}
	//			}
	//			
			}
			// 예외 처리
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
        }
	}
	
	private void close() throws IOException
	{
		clientSocket.close();
		System.out.println("Connection Closed");
	}
}
