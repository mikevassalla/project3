import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.Label;



public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	private Consumer<Serializable> callback;
	private String ip;
	private int port;
	
	
	//Client(Consumer<Serializable> call,
	Client(String ip, int port, Consumer<Serializable> call){
		this.ip = ip;
		this.port = port;
		callback = call;
	}
	
	public void run() {
		try {
		socketClient= new Socket(ip, port);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}
		
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
