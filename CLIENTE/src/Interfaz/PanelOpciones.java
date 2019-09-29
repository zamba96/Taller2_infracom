package Interfaz;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PanelOpciones extends JPanel implements ActionListener{
	
	private InterfazCliente interfaz;
	
	private JButton conectar;
	public final static String CONECTAR = "CONECTAR";
	
	private JButton desconectar;
	public final static String DESCONECTAR = "DESCONECTAR";
	
	private JLabel state;
	
	public PanelOpciones(InterfazCliente principal){
		interfaz = principal;
		
        TitledBorder border = new TitledBorder( "Opciones" );
        border.setTitleColor( Color.BLUE );
        setBorder( border );
        
        setLayout( new GridLayout( 1, 3 ) );
        
        state = new JLabel("");
        state.setIcon( new ImageIcon( interfaz.getConectionState() ) );
        add(state);
        
        conectar = new JButton( "Conectar" );
        conectar.setActionCommand( CONECTAR );
        conectar.addActionListener(this);
        add(conectar);
        
        
        desconectar = new JButton( "Desconectar" );
        desconectar.setActionCommand( DESCONECTAR );
        desconectar.addActionListener(this);
        add(desconectar);
        
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand( );
        
        if( comando.equals( CONECTAR ) )
        {
        	interfaz.connect();
        }
	}

}
