package server;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class ClienteThread extends Thread{

	public final static String LISTO = "LISTO";

	public final static String ERROR = "ERROR";

	public final static String ACK = "ACK";

	public final static int BUFFER_SIZE = 1024;
	
	public final static String OK = "OK";

	private Client client;

	private Server main;

	private byte[] buffer;

	private byte[] bytes;


	public ClienteThread(Client client, Server main, byte[] bytes) {
		this.client = client;
		this.main = main;
		this.bytes = bytes;
		buffer = new byte[BUFFER_SIZE];
	}


	public void run() {
		System.out.println("Starting client thread for client");
		try {
			//listo y ack del listo
			String line = client.read();
			System.out.println(line);
			if(line.equals(LISTO)) {
				client.sendString(ACK);
				boolean listo = main.registrarListo();

				synchronized (main) {
					if(!listo) {
						main.wait();
					}
				}

			}else {
				client.sendString(ERROR + ":Se esperaba el estado listo");
				client.cerrarConexion();
				return;
			}
			int pos = 0;
			//envio del archivo
			client.sendString("BYTES:" + bytes.length);
			System.out.println("BYTES:" + bytes.length);
			long inicio = System.currentTimeMillis();
			for(byte b:bytes) {
				buffer[pos] = b;
				pos++;
				if(pos == BUFFER_SIZE) {
					client.sendBytes(buffer);
					pos = 0;
					buffer = new byte[BUFFER_SIZE];
				}
			}
			System.out.println("enviados");
			//envio del hash md5
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(bytes);
				byte[] digest = md.digest();
				String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
				client.sendString("HASH:" + hash);
				
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				client.cerrarConexion();
				return;
			}
			
			//recivir el ok
			boolean exito;
			long duracion = -1;
			line = client.read();
			System.out.println(line);
			if(line.contentEquals(OK)) {
				duracion = System.currentTimeMillis() - inicio;
				exito = true;
			}else {
				duracion = System.currentTimeMillis() - inicio;
				exito = false;
			}
			
			Server.threadLogger.info("==================================================" + 
								"\nCliente: " + client.getId() + 
								"\nExito:" + exito + 
								"\nduracion: " + duracion +"ms " + duracion/1000 + " segundos");
			
			//se elimina de los clientes y se cierra
			client.cerrarConexion();
			main.deleteClient(this);
			

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
