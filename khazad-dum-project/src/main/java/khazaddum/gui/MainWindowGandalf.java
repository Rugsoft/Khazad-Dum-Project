package khazaddum.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import khazaddum.modelo.Empleado;
import khazaddum.modelo.VisitaTemporal;
import khazaddum.operaciones.ComunicacionSerie.SerialDataCallback;
import khazaddum.operaciones.ConexionDB;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.JButton;

public class MainWindowGandalf extends JFrame implements ActionListener, SerialDataCallback {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JLabel lblSaludoGandalf;
	private JButton btnAñadirEmp;
	private DefaultTableModel modelo;
	private ArrayList<Empleado> empList;
	private ArrayList<VisitaTemporal> tempList;

	
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
		
		elementosVisuales();
		saludo(user, nivel);
		cargarTablaEmpleados();
		
	}
	
	

	private void saludo(String user, String nivel) {
		
		lblSaludoGandalf.setText("Bienvenido " + user + " - Nivel: " + nivel);
		
	}


	private void elementosVisuales() {
		contentPane.setLayout(null);
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 306, 245);
		panel.setBackground(new Color(192, 192, 192));
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(74, 13, 144, 124);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(MainWindowGandalf.class.getResource("/kazaddum/images/Logo2-removebg-250px.png")));
		panel.add(lblNewLabel);
		
		lblSaludoGandalf = new JLabel("");
		lblSaludoGandalf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSaludoGandalf.setHorizontalAlignment(SwingConstants.CENTER);
		lblSaludoGandalf.setBounds(27, 173, 254, 38);
		panel.add(lblSaludoGandalf);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(308, 0, 1080, 723);
		panel_1.setBackground(new Color(128, 128, 255));
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 1056, 697);
		panel_1.add(scrollPane);
		
		table = new JTable(modelo);
		scrollPane.setViewportView(table);
		
		btnAñadirEmp = new JButton("");
		btnAñadirEmp.setBorder(null);
		btnAñadirEmp.setIcon(new ImageIcon(MainWindowGandalf.class.getResource("/kazaddum/images/BotonAñadirEmpleado-removebg-250px.png")));
		btnAñadirEmp.setBackground(new Color(0, 128, 128));
		btnAñadirEmp.setBounds(30, 253, 237, 93);
		btnAñadirEmp.addActionListener(this);
		contentPane.add(btnAñadirEmp);
		
	}


	@Override
	public void onDatoRecibido(String dato) {
		// TODO Auto-generated method stub
		
	}
	
	private void cargarTablaEmpleados() {
		
		empList = null;

		modelo = new DefaultTableModel();
		modelo.addColumn("Nombre");
		modelo.addColumn("Apellido");
		modelo.addColumn("Apellido 2");
		modelo.addColumn("DNI");
		modelo.addColumn("Género");
		modelo.addColumn("Puesto");
		modelo.addColumn("Email");
		modelo.addColumn("Nivel de Acceso");
		empList = new ArrayList<Empleado>();
		String sql = "SELECT * FROM empleados";
		
		try {
			ConexionDB conexion = new ConexionDB();
			empList = conexion.obtenerEmpleados(sql);
			for (Empleado emp : empList) {
				modelo.addRow(emp.crear());
			}
			table.setModel(modelo);
			
		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (btnAñadirEmp == e.getSource()) {
			EmployeeRegister empReg = new EmployeeRegister();
			empReg.setVisible(true);
			empReg.setLocationRelativeTo(null);
			empReg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		
		
	}
}
