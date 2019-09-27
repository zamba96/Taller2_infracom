package server;

public class ClienteThread extends Thread{

	
	private Client client;
	
	private Main main;
	
	
	
	public ClienteThread(Client client, Main main) {
		this.client = client;
		this.main = main;
		
	}
	
	
	public void run() {
		System.out.println("Starting client thread for client");
		
	}
	
}
