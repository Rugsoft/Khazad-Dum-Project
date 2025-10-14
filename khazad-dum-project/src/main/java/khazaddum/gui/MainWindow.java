package khazaddum.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList list;
	private JLabel lblPicture;
	private JTextField textName;
	private JTextField textLastName;
	private JTextField textDNI;
	private JTextField textRole;
	private JTextField textField;

	public MainWindow() {
		setUndecorated(true);
		setResizable(false);
		setTitle("Khazzad-Dûm Pro");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 1044, 588);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(158, 182, 226));
		panel.setBounds(0, 0, 287, 588);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(MainWindow.class.getResource("/kazaddum/images/Logo2-removebg-250px.png")));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(76, 10, 124, 134);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Historial de hoy");
		lblNewLabel_2.setFont(new Font("Consolas", Font.PLAIN, 14));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(34, 154, 214, 28);
		panel.add(lblNewLabel_2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 184, 267, 394);
		panel.add(scrollPane);
		
		list = new JList();
		list.setBackground(new Color(192, 192, 192));
		scrollPane.setViewportView(list);
		
		JLabel lblNewLabel = new JLabel("X");
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(getDefaultCloseOperation());
			}
		});
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 30));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(1005, 0, 39, 39);
		contentPane.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(158, 182, 226));
		panel_1.setBounds(287, 183, 757, 405);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		lblPicture = new JLabel("");
		lblPicture.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblPicture.setHorizontalAlignment(SwingConstants.CENTER);
		lblPicture.setBounds(10, 10, 148, 171);
		panel_1.add(lblPicture);
		
		JLabel lblName = new JLabel("Nombre");
		lblName.setBounds(181, 21, 57, 12);
		panel_1.add(lblName);
		
		textName = new JTextField();
		textName.setEditable(false);
		textName.setBorder(null);
		textName.setHorizontalAlignment(SwingConstants.LEFT);
		textName.setBackground(new Color(158, 182, 226));
		textName.setBounds(241, 18, 113, 18);
		panel_1.add(textName);
		textName.setColumns(10);
		
		JLabel lblApellido = new JLabel("Apellido");
		lblApellido.setBounds(181, 46, 57, 15);
		panel_1.add(lblApellido);
		
		textLastName = new JTextField();
		textLastName.setHorizontalAlignment(SwingConstants.LEFT);
		textLastName.setEditable(false);
		textLastName.setBackground(new Color(158, 182, 226));
		textLastName.setBorder(null);
		textLastName.setColumns(10);
		textLastName.setBounds(241, 43, 113, 18);
		panel_1.add(textLastName);
		
		JLabel lblDni = new JLabel("DNI");
		lblDni.setBounds(181, 71, 57, 12);
		panel_1.add(lblDni);
		
		textDNI = new JTextField();
		textDNI.setHorizontalAlignment(SwingConstants.LEFT);
		textDNI.setBorder(null);
		textDNI.setBackground(new Color(158, 182, 226));
		textDNI.setEditable(false);
		textDNI.setColumns(10);
		textDNI.setBounds(241, 68, 113, 18);
		panel_1.add(textDNI);
		
		JLabel lblCargo = new JLabel("Cargo");
		lblCargo.setBounds(181, 96, 57, 15);
		panel_1.add(lblCargo);
		
		textRole = new JTextField();
		textRole.setHorizontalAlignment(SwingConstants.LEFT);
		textRole.setBackground(new Color(158, 182, 226));
		textRole.setBorder(null);
		textRole.setEditable(false);
		textRole.setColumns(10);
		textRole.setBounds(241, 93, 113, 18);
		panel_1.add(textRole);
		
		JLabel lblSecurityLevel = new JLabel("Nivel de seguridad");
		lblSecurityLevel.setBounds(181, 121, 121, 16);
		panel_1.add(lblSecurityLevel);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setEditable(false);
		textField.setBorder(null);
		textField.setBackground(new Color(158, 182, 226));
		textField.setColumns(10);
		textField.setBounds(312, 121, 42, 18);
		panel_1.add(textField);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(192, 192, 192));
		panel_2.setBounds(287, 0, 757, 183);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

	}
}
