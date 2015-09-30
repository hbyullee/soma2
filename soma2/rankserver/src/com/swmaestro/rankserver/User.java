package com.swmaestro.rankserver;

import java.net.Socket;



public class User {

	int score;
	int age;
	int sex;
	Socket clientSocket;
	
	public User(Socket clientSocket, int score, int age, int sex) {
	
		this.score = score;
		this.age   = age;
		this.sex   = sex;
		this.clientSocket = clientSocket;
		
	}
	
	public int getScore() {
		return score;
	}
	
	public int getAge() {
		return age;
	}
	
	public int getSex() {
		return sex;
	}
	
	public Socket getClientSocket() {
		return clientSocket;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public void setSex(int sex) {
		this.sex = sex;
	}
	
	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	
//	@Override
//	public int compareTo(User user) {
//			if (this.score < user.score) {
//				return -1;
//			} else if (this.score == user.score) {
//				return 0;
//			} else {
//				return 1;
//			}
//	}
}
