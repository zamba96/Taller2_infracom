package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.bind.DatatypeConverter;


public class Server {

	private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
	
	public static void main(String[] args) {
		
		//LOGGER.info("Iniciando Server...");
		LOGGER.info("Iniciaasdfsdfndo Server");

		FileHandler fh;
		try {
			fh = new FileHandler("./logs/ServerLog.log");
			LOGGER.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(System.in);

		int numCon1 = -1;
		System.out.println("Ingrese el numero de clientes que desea esperar");
		try {
			numCon1 = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			LOGGER.severe("Por favor ingrese un numero entero");
			LOGGER.severe("Terminando ejecucion");
			System.exit(1);
		}
		
		ArrayList<File> files = new ArrayList<File>();
		try {
			File folder = new File("./data/");
			int i = 1;
			for (File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					
				} else {
					files.add(fileEntry);
					System.out.println(i + ". " + fileEntry.getName());
				}
			}
		} catch (Exception e) {
			LOGGER.severe("Error al leer archivos del directorio ./data/");
			System.exit(1);
		}

		int numFile = -1;
		System.out.println("Ingrese el numero del archivo que desea enviar");
		try {
			numFile = Integer.parseInt(sc.nextLine());
			LOGGER.info("Se usara el archivo: " + files.get(numFile - 1).toString());
		} catch (NumberFormatException e) {
			LOGGER.severe("Por favor ingrese un numero entero");
			LOGGER.severe("Terminando ejecucion");
			System.exit(1);
		}
		sc.close();
		
		Server main = new Server(numCon1, files.get(numFile - 1));
		main.recieveConnections();
	}
	
	/**
	 * bytes del archivo que se desea mandar
	 */
	private byte[] bytes;

	/**
	 * maximo de conexiones
	 */
	private int numCon;
	/**
	 * arreglo de sockets
	 */
	private ArrayList<ClienteThread> clients;

	/**
	 * socket del servidor para recivir conexiones
	 */
	private ServerSocket serverSocket;

	/**
	 * indica cual es el siguiente id;
	 */
	private int ids;
	
	/**
	 * numero de clientes listos
	 */
	private int listos;

	/**
	 * crea un nuevo  main
	 * @param numCon1 numero de conexiones que espera el server para enviar vainas
	 * @param pArch	archivo que se desea enviar
	 */
	public Server(int numCon1, File pArch) {
		clients = new ArrayList<ClienteThread>();
		try {
			serverSocket = new ServerSocket(4200);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		numCon = numCon1;
		ids = 1;
		listos = 0;
		
		
		try {
			FileInputStream fi = new FileInputStream(pArch);
			bytes = new byte[(int) pArch.length()];
			fi.read(bytes);
			fi.close();
			//Hash, para efectos de verlo
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytes);
			byte[] digest = md.digest();
			String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
			LOGGER.info("HASH" + hash);
		} catch (IOException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/**
	 * recive las conexiones entrantes
	 */
	public void recieveConnections() {
		LOGGER.info("ServerSocket: " + serverSocket.toString());
		while(true){
			
			Socket tempSocket = null;

			try {
				tempSocket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(clients.size() >= numCon && tempSocket != null) {
				LOGGER.info("Connection with client at:" + 
						tempSocket.getInetAddress() + " refused, max connections reached");
				try {
					tempSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if( tempSocket != null) {
				LOGGER.info("Connection accepted:");
				LOGGER.info(tempSocket.toString());
				Client client = new Client(tempSocket, ids++);
				ClienteThread ct = new ClienteThread(client, this, bytes);
				clients.add(ct);
				ct.start();
				



			} else {
				LOGGER.severe("exception while creating socket");
			}


		}
	}

	/**
	 * se encarga de borrar un cliente cuando este ha terminado
	 * @param client el cliente que desea borrar
	 */
	public void deleteClient(ClienteThread client) {
		clients.remove(client);
	}

	/**
	 * registra un cliente como listo
	 * @return true si ya se encuentran todos listos, false de lo contrario
	 */
	public synchronized boolean registrarListo() {
		listos++;
		if(listos == numCon) {
			this.notifyAll();
			listos = 0;
			return true;
		}
		return false;
	}



}
