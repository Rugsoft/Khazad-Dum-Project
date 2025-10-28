package khazaddum.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import khazaddum.operaciones.ComunicacionSerie.SerialDataCallback;

public class MainWindowGandalf extends JFrame implements ActionListener, SerialDataCallback {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	
	public MainWindowGandalf(String user, String nivel) {
		setTitle("Master of Puppets");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 1011, 577);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		elementosVisuales();
	}


	private void elementosVisuales() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onDatoRecibido(String dato) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
