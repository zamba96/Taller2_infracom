package Back;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class Connection extends Thread{

	private Socket socket            = null; 
	private BufferedReader  in   = null; 
	private PrintWriter out     = null;
	private String ip;
	private int port;

	public Connection(String pip, int pport){
		ip = pip;
		port = pport;
	}

	public void run(){

		//Starting connection
		try{
			socket = new Socket(ip, port); 
			System.out.println("Connected");
			in = new BufferedReader( new InputStreamReader( socket.getInputStream( ) ) );
			out = new PrintWriter( socket.getOutputStream( ), true );
			out.println("LISTO");
			System.out.println("LISTO");
		} 
		catch(UnknownHostException u){
			System.out.println(u); 
		} 
		catch(IOException i){
			System.out.println(i); 
		}

		//Reading initial line
		String line = null;
		try {
			line = in.readLine();
			System.out.println(line);
		} catch (IOException e1) {
			System.out.println("Error");
		}
		if(!line.equals("ACK")){
			// close the connection 
			try
			{ 
				in.close(); 
				out.close(); 
				socket.close(); 
			} 
			catch(IOException i) 
			{ 
				System.out.println(i); 
			} 
		}else{
			while(!line.equals("END")){
				if(line.split(":")[0].equals("BYTES")){
					int n = Integer.parseInt(line.split(":")[1]);
					byte[] bs = new byte[n];
					for (int i = 0; i < n; i++) {
						try {
							bs[i] = (byte) socket.getInputStream().read();
						} catch (IOException e) {
							System.out.println("Error en lectura del archivo");
						}
					}
					try {
						MessageDigest md = MessageDigest.getInstance("MD5");
						md.update(bs);
						byte[] digest = md.digest();
						String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
						line = in.readLine();
						String hss = line.split(":")[1];
						if(hash.equals(hss)){
							out.print("OK"); 
						}else{
							out.print("ERROR:Hash missmatch");
						}
					} catch (NoSuchAlgorithmException e) {

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// close the connection 
			try
			{ 
				in.close(); 
				out.close(); 
				socket.close(); 
			} 
			catch(IOException i) 
			{ 
				System.out.println(i); 
			} 
		}


	}

}
