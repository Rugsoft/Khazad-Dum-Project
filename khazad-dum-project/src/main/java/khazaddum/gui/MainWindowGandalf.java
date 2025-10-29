package khazaddum.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import khazaddum.operaciones.ComunicacionSerie.SerialDataCallback;
import java.awt.Rectangle;
import java.awt.Frame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MainWindowGandalf extends JFrame implements ActionListener, SerialDataCallback {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	
	public MainWindowGandalf(String user, String nivel) {
		setResizable(false);
		setTitle("Master of Puppets");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1404, 762);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null); // Centra la ventana
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 306, 245);
		panel.setBackground(new Color(192, 192, 192));
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(MainWindowGandalf.class.getResource("/kazaddum/images/Logo2-removebg-250px.png")));
		lblNewLabel.setBounds(74, 13, 144, 124);
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(308, 0, 1080, 723);
		panel_1.setBackground(new Color(128, 128, 255));
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 1056, 697);
		panel_1.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);

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
