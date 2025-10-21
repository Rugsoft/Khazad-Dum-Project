package khazaddum.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import khazaddum.modelo.Empleado;
import khazaddum.operaciones.ConexionDB;

import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import javax.swing.JDialog;

public class MainWindowBalrog extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblPicture;
	private JTable table;
	private JTextField textName;
	private JTextField textLastName1;
	private JTextField textLasName2;
	private JTextField textDNI;
	private JTextField textEmail;
	private JTextField textGender;
	private JTextField textRole;
	private JTextField textAcces;
	private JTextField textHour;
	private JLabel lblSaludo;
	private JPanel panel_2;
	private DefaultTableModel modelo;
	private JButton btnTempUser;

	public MainWindowBalrog(String user, String nivel) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		visualElements();
		saludo(user, nivel);
		
		

	}

	private void saludo(String user, String nivel) {
		
		lblSaludo.setText("Bienvenido " + user + " - Nivel: " + nivel);
		
	}

	private void visualElements() {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(158, 182, 226));
		panel.setBounds(0, 0, 287, 588);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(MainWindowBalrog.class.getResource("/kazaddum/images/Logo2-removebg-250px.png")));
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
		
		
		modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Entrada/Salida");
		table = new JTable(modelo);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				prepararUsuario();
			}
		});
		scrollPane.setViewportView(table);
		
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
		panel_1.setBackground(new Color(128, 128, 255));
		panel_1.setBounds(287, 183, 757, 405);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		lblPicture = new JLabel("");
		lblPicture.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblPicture.setHorizontalAlignment(SwingConstants.CENTER);
		lblPicture.setBounds(14, 33, 148, 171);
		panel_1.add(lblPicture);
		
		textName = new JTextField();
		textName.setEditable(false);
		textName.setBackground(new Color(128, 128, 255));
		textName.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Nombre", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textName.setBounds(195, 59, 148, 34);
		panel_1.add(textName);
		textName.setColumns(10);
		
		textLastName1 = new JTextField();
		textLastName1.setEditable(false);
		textLastName1.setColumns(10);
		textLastName1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "1er Apellido", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textLastName1.setBackground(new Color(128, 128, 255));
		textLastName1.setBounds(353, 59, 148, 34);
		panel_1.add(textLastName1);
		
		textLasName2 = new JTextField();
		textLasName2.setEditable(false);
		textLasName2.setColumns(10);
		textLasName2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "2do Apellido", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textLasName2.setBackground(new Color(128, 128, 255));
		textLasName2.setBounds(511, 59, 148, 34);
		panel_1.add(textLasName2);
		
		textDNI = new JTextField();
		textDNI.setEditable(false);
		textDNI.setColumns(10);
		textDNI.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "DNI", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textDNI.setBackground(new Color(128, 128, 255));
		textDNI.setBounds(195, 103, 148, 34);
		panel_1.add(textDNI);
		
		textEmail = new JTextField();
		textEmail.setEditable(false);
		textEmail.setColumns(10);
		textEmail.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Email", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textEmail.setBackground(new Color(128, 128, 255));
		textEmail.setBounds(353, 103, 148, 34);
		panel_1.add(textEmail);
		
		textGender = new JTextField();
		textGender.setEditable(false);
		textGender.setColumns(10);
		textGender.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Genero", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textGender.setBackground(new Color(128, 128, 255));
		textGender.setBounds(511, 103, 148, 34);
		panel_1.add(textGender);
		
		textRole = new JTextField();
		textRole.setEditable(false);
		textRole.setColumns(10);
		textRole.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Puesto", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textRole.setBackground(new Color(128, 128, 255));
		textRole.setBounds(195, 147, 148, 34);
		panel_1.add(textRole);
		
		textAcces = new JTextField();
		textAcces.setEditable(false);
		textAcces.setColumns(10);
		textAcces.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "N.Acceso", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textAcces.setBackground(new Color(128, 128, 255));
		textAcces.setBounds(353, 147, 148, 34);
		panel_1.add(textAcces);
		
		textHour = new JTextField();
		textHour.setEditable(false);
		textHour.setColumns(10);
		textHour.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Hora Entrada/Salida", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textHour.setBackground(new Color(128, 128, 255));
		textHour.setBounds(511, 147, 148, 34);
		panel_1.add(textHour);
		
		btnTempUser = new JButton("Registrar Usuario temporal");
		btnTempUser.addActionListener(this);
		btnTempUser.setBounds(457, 353, 225, 20);
		panel_1.add(btnTempUser);
		
		panel_2 = new JPanel();
		panel_2.setBackground(new Color(192, 192, 192));
		panel_2.setBounds(287, 0, 757, 183);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		lblSaludo = new JLabel("");
		lblSaludo.setHorizontalAlignment(SwingConstants.CENTER);
		lblSaludo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSaludo.setBounds(55, 37, 619, 63);
		panel_2.add(lblSaludo);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(btnTempUser == e.getSource()) {
			
			usuarioTemporal();
		}
		
	}
	
	private void usuarioTemporal() {
		
		TemporaryUserRegister dialog = new TemporaryUserRegister();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}

	private void prepararUsuario() {
		
		int fila = table.getSelectedRow();
		
		if(fila != -1) {
			
			String nombre = modelo.getValueAt(fila, 0).toString();
			String apellido1 = modelo.getValueAt(fila, 1).toString();
			String entradaSalida = modelo.getValueAt(fila, 2).toString();
			
			ConexionDB conex = new ConexionDB();
			Empleado empleado = conex.buscarEmpleadoPorNombredeTabla(nombre, apellido1);
			
			Object[] datos = empleado.crear();
			textName.setText(String.valueOf(datos[0]));
			textLastName1.setText(String.valueOf(datos[1]));
			textLasName2.setText(String.valueOf(datos[2]));
			textDNI.setText(String.valueOf(datos[3]));
			textGender.setText(String.valueOf(datos[4]));
			textRole.setText(String.valueOf(datos[5]));
			textEmail.setText(String.valueOf(datos[6]));
			textAcces.setText(String.valueOf(datos[7]));
			
			if (datos[8] != null && datos[8] instanceof byte[]) {
				
			    byte[] fotoBytes = (byte[]) datos[8];
			    javax.swing.ImageIcon icono = new javax.swing.ImageIcon(fotoBytes);
			    lblPicture.setIcon(icono); 
			    
			} else {
			   
			    lblPicture.setIcon(null); 
			}
			
			textHour.setText(entradaSalida);
		
		
		}

	}
}	
