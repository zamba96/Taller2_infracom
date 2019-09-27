package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Main {

	public static void main(String[] args) {
		System.out.println("Starting Server...");
		Main main = new Main();
		main.recieveConnections();
	}

	/**
	 * maximo de conexiones
	 */
	public final static int MAX_CONECTIONS = 25;
	
	/**
	 * arreglo de sockets
	 */
	private ArrayList<Client> clients;
	
	/**
	 * socket del servidor para recivir conexiones
	 */
	private ServerSocket serverSocket;
	
	/**
	 * indica si el servidor esta en modo test
	 */
	private boolean testMode;
	
	/**
	 * indica el numero de conexiones activas que debe esperarel servidor en modo test
	 */
	private int testConnectios;
	
	/**
	 * indica cual es el siguiente id;
	 */
	private int ids;
	
	private ArrayList<ClienteThread> testThreads;
	
	public Main() {
		clients = new ArrayList<Client>();
		try {
			serverSocket = new ServerSocket(420);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setTestMode(false);
		ids = 1;
	}
	
	
	
	/**
	 * recive las conexiones entrantes
	 */
	public void recieveConnections() {
		while(true){
			Socket tempSocket = null;
			
			try {
				tempSocket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(clients.size() >= MAX_CONECTIONS && tempSocket != null) {
				System.out.println("Connection with client at:" + 
									tempSocket.getInetAddress() + " refused, max connections reached");
			} else if( tempSocket != null) {
				Client client = new Client(tempSocket, ids);
				ids += 1;
				clients.add(client);
				
				if(testMode) {
					//crea el arreglo
					if(testThreads == null) {
						testThreads = new ArrayList<ClienteThread>();
					}
					
					
					
				}
				
				
			} else {
				System.out.println("[ERROR]: exception while creating socket");
			}
			
			
		}
	}

	/**
	 * se encarga de borrar un cliente cuando este ha terminado
	 * @param client el cliente que desea borrar
	 */
	public void deleteClient(Client client) {
		clients.remove(client);
	}

	/**
	 * Indica si el servidor esta en test mode
	 * @return true si esta en modo test, false de lo contrario
	 */
	public boolean isTestMode() {
		return testMode;
	}


	/**
	 * activa o desactiva el modo test
	 * @param testMode true si se quiere poner en modo test, false si se quiere desactivar el modo test
	 */
	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}
	
	
	
}
