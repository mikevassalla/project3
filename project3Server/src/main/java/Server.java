import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server{
	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	
	
	Server(Consumer<Serializable> call){
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		public void run() {
			//dealer.dealerAddCards(deck.getDeck());
			
			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept(new Responses(1, "client has connected to server: " + "client #" + count));
				clients.add(c);
				c.start();
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
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
				
				updateClients("new client on server: client #"+count);
				if(count == 2) {
					//try {
						try {
							//Enable Buttons
							clients.get(0).out.writeObject(new Responses(1));
							clients.get(0).out.writeObject(new Responses(20, "p1"));
							clients.get(1).out.writeObject(new Responses(1));
							clients.get(1).out.writeObject(new Responses(20, "p2"));
						} catch (IOException e) {
							e.printStackTrace();
						}
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
				}
					
				while(true) {
					try {
						Responses r = (Responses)in.readObject();
					    callback.accept(r);
					}
					catch(Exception e) {
						callback.accept(new Responses(400, "Client " + count + " has left!"));
					    clients.remove(this);
					    break;
					}
				}
			}//end of run
		}//end of client thread
		public void sendResponse(Responses message, String p) {
			if(p.equals("p1")) {
				ClientThread t = clients.get(0);
				try {
				 t.out.writeObject(message);
				}
				catch(Exception e) {
					System.out.println(e);
				}
			}
			else if(p.equals("p2")) {
				ClientThread t = clients.get(1);
				try {
				 t.out.writeObject(message);
				}
				catch(Exception e) {
					System.out.println(e);
				}
			}
		}
		
}


	
	

	
