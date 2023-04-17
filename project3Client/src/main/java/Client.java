//  Project 3 -  3 Card Poker
//  Daniel Beben - Dbeben2 & Micheal Vassalla mvassa4
//  CS342 Spring 2023
// This project you will implement a networked version of the popular casino game 3 Card Poker.
// The focus of the project is event driven programing and networking with Java Sockets.

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.Label;



public class Client extends Thread {

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	private Consumer<Serializable> callback;
	private String ip;
	private int port;
	

	Client(String ip, int port, Consumer<Serializable> call){
		this.ip = ip;
		this.port = port;
		callback = call;
	}
	
	public void run() {
		try {
		socketClient = new Socket(ip, port);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {System.out.println(e);}
		
		while(true) {
			 
			try {
				Responses r = (Responses) in.readObject();
				callback.accept(r);
			}
			catch(Exception a) {}
		}
	
    }
	
	public void send(Responses data) {
		
		try {
			out.writeObject(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void setCallback(Consumer<Serializable> callback) {
		this.callback = callback;
	}


}
