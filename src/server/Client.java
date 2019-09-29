package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	/**
	 * socket de conceccion con el cliente
	 */
	private Socket socket;
	
	/**
	 * lector del cliente
	 */
	private BufferedReader reader;
	
	/**
	 * escritor del cliente
	 */
	private PrintWriter writer;
	
	/**
	 * indica el id del cliente
	 */
	private int id;
	
	/**
	 * crea un nuevo cliente, asignando el writer y el reader del socket correspondiente
	 * @param s socket del cliente
	 */
	public Client(Socket s, int id) {
		socket = s;
		this.id = id;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * envia un string al cliente
	 * @param s el string a enviar
	 */
	public void sendString(String s) {
		writer.write(s);
		writer.flush();
	}
	
	
	public String read() throws IOException {
		byte b = (byte) socket.getInputStream().read();
		return reader.readLine();
		
	}
	
	/**
	 * gets the id of the client
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public void cerrarConexion() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendBytes(byte[] bytes) throws IOException {
		socket.getOutputStream().write(bytes);
	}
	
}
