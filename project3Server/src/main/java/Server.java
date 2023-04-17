//  Project 3 -  3 Card Poker
//  Daniel Beben - Dbeben2 & Micheal Vassalla mvassa4
//  CS342 Spring 2023
// This project you will implement a networked version of the popular casino game 3 Card Poker.
// The focus of the project is event driven programing and networking with Java Sockets.

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
public class Server{
	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	private Integer port;
	
	Server(Integer port, Consumer<Serializable> call){
		callback = call;
		server = new TheServer();
		server.start();
		this.port = port;
	}
	
	
	public class TheServer extends Thread{
		public void run() {
			try(ServerSocket mysocket = new ServerSocket(port);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
				ClientThread c = new ClientThread(mysocket.accept(), count);
				clients.add(c);
				callback.accept(new Responses(1, "client has connected to server: " + "client #" + count));
				c.start();
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					System.out.println(e);
					callback.accept(new Responses(404, "Server socket did not launch"));
				}
			}//end of while
		}
	

		class ClientThread extends Thread{

			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}
			
			public void updateClients(String message) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					 t.out.writeObject(message);
					}
					catch(Exception e) {}
				}
			}
			public void sendRespons(Responses message, String p1) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					 t.out.writeObject(message);
					}
					catch(Exception e) {}
				}
			}
			
			public void run(){	
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				
				//updateClients("new client on server: client #"+count);
						/*
						deck.shuffleCards();
						p1.playerAddCards(deck.dealCard());
						clients.get(0).out.writeObject(new Responses(1, p1.cards));
						p2.playerAddCards(deck.dealCard());
						clients.get(1).out.writeObject(new Responses(1, p2.cards));
						*/
					//} catch (IOException m) {
						// TODO Auto-generated catch block
						//m.printStackTrace();
					//}
				callback.accept(new Responses(21));
				while(true) {
					try {
						Responses r = (Responses)in.readObject();
					    callback.accept(r);
					}
					catch(Exception e) {
						callback.accept(new Responses(400, "Client " + count + " has left!"));
					    //clients.remove(this);
					    break;
					}
				}
			}//end of run
		}//end of client thread
		public void sendResponse(Responses message, Integer p) {
			try {
				clients.get(p).out.writeObject(message);
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
}


	
	

	
