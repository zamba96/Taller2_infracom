package Interfaz;

import java.awt.BorderLayout;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Back.Connection;

public class InterfazCliente extends JFrame{
	
	public final static String RUTA_IMAGEN = "";
	public final static String RUTA_IMAGEN_C = "./../data/conectado.png";
	public final static String RUTA_IMAGEN_D = "./../data/desconectado.png";
	
	private Connection c;
	
	private PanelOpciones op;
	
	private String connectionState;
	
	public InterfazCliente(){
		setTitle( "FTP de los ALPES" );
		setSize( 600, 700 );
		setLocationRelativeTo( null );
		setResizable( false );
		setDefaultCloseOperation( EXIT_ON_CLOSE );

		setLayout( new BorderLayout( ) );
		c = null;
		connectionState = RUTA_IMAGEN_C;
		
		// Imagen del título
		JLabel labImagen = new JLabel( );
		labImagen.setIcon( new ImageIcon( RUTA_IMAGEN ) );
		add( labImagen, BorderLayout.NORTH );
		
		op = new PanelOpciones(this);
		add( op, BorderLayout.SOUTH );
	}
	
	public void connect(){
		String str = JOptionPane.showInputDialog(this, "Digita la IP y el puerto de la siguiente manera: IP:Puerto");
		String ip = str.split(":")[0];
		int port;
		try{
			port = Integer.parseInt(str.split(":")[1]);
		}catch(Exception e){
			port = 21;
		}
		c = new Connection(ip, port);
		c.start();
	}
	
	public String getConectionState(){
		return connectionState;
	}

}
