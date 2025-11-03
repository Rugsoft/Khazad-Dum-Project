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
import khazaddum.operaciones.ConexionDB;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JTextField;
import com.github.lgooddatepicker.components.DatePicker;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

public class MainWindowGandalf extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaPrincipal;
	private JLabel lblSaludoGandalf;
	private JButton btnAñadirEmp;
	private DefaultTableModel modeloP;
	private DefaultTableModel modeloR;
	private ArrayList<Empleado> empList;
	private ArrayList<VisitaTemporal> tempList;
	private JTabbedPane tabbedPane;
	private JTable tablaRegistros;
	private JTextField textBusqueda;
	private JComboBox<String> comboBusquedaTipo;
	private DatePicker datePickerInicio;
	private DatePicker datePickerFinal;
	private JButton btnBuscar;
	private JButton btnGuardar;
	private JButton btnEliminar;
	private JButton btnExportPDF;
	private JButton btnExportExcel;
	private String tipoEntidad = "empleado"; // "empleado" o "temporal"
	private JRadioButton radioEmp;
	private JRadioButton radioTemp;
	private JCheckBox checkAll;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	
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
		//cargarTablaTemporales();
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
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 13, 1056, 697);
		panel_1.add(tabbedPane);
		
		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("Gestión de Empleados", null, scrollPane, null);
		
		tablaPrincipal = new JTable(modeloP);
		scrollPane.setViewportView(tablaPrincipal);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab("Registro de Entradas/Salidas", null, scrollPane_1, null);
		
		tablaRegistros = new JTable(modeloR);
		scrollPane_1.setViewportView(tablaRegistros);
		panel_1.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{tabbedPane, scrollPane, tablaPrincipal, scrollPane_1, tablaRegistros}));
		
		btnAñadirEmp = new JButton("");
		btnAñadirEmp.setBorder(null);
		btnAñadirEmp.setIcon(new ImageIcon(MainWindowGandalf.class.getResource("/kazaddum/images/BotonAñadirEmpleado-removebg-250px.png")));
		btnAñadirEmp.setBackground(new Color(0, 128, 128));
		btnAñadirEmp.setBounds(30, 253, 237, 93);
		btnAñadirEmp.addActionListener(this);
		contentPane.add(btnAñadirEmp);
		
		JPanel lineaSeparadoraPanel = new JPanel();
		lineaSeparadoraPanel.setBackground(new Color(0, 0, 0));
		lineaSeparadoraPanel.setBounds(0, 346, 308, 3);
		contentPane.add(lineaSeparadoraPanel);
		
		JLabel lblNewLabel_1 = new JLabel("Buscar Empleado");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(40, 352, 210, 20);
		contentPane.add(lblNewLabel_1);
		
		comboBusquedaTipo = new JComboBox<String>();
		comboBusquedaTipo.setModel(new DefaultComboBoxModel<String>(new String[] {"Seleccione", "Por Nombre", "Por DNI"}));
		comboBusquedaTipo.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Nombre/DNI", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		comboBusquedaTipo.setBackground(new Color(64, 128, 128));
		comboBusquedaTipo.setRenderer(new CustomColorRenderer());
		comboBusquedaTipo.setBounds(10, 382, 89, 42);
		contentPane.add(comboBusquedaTipo);
		
		textBusqueda = new JTextField();
		textBusqueda.setColumns(10);
		textBusqueda.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Nombre/DNI", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textBusqueda.setBackground(new Color(0, 128, 128));
		textBusqueda.setBounds(128, 382, 139, 42);
		contentPane.add(textBusqueda);
		
		datePickerInicio = new DatePicker();
		datePickerInicio.setBorder(null);
		datePickerInicio.setBounds(10, 449, 201, 18);
		contentPane.add(datePickerInicio);
		
		datePickerFinal = new DatePicker();
		datePickerFinal.setBounds(10, 489, 201, 18);
		contentPane.add(datePickerFinal);
		
		JLabel lblNewLabel_2 = new JLabel("Fecha Incio");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(94, 434, 89, 12);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("Fecha Final");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setBounds(94, 477, 89, 12);
		contentPane.add(lblNewLabel_2_1);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(16, 527, 168, 20);
		contentPane.add(btnBuscar);
		
		JPanel lineaSeparadoraPanel_1 = new JPanel();
		lineaSeparadoraPanel_1.setBackground(Color.BLACK);
		lineaSeparadoraPanel_1.setBounds(0, 564, 308, 3);
		contentPane.add(lineaSeparadoraPanel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Funciones Extras");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(40, 573, 210, 20);
		contentPane.add(lblNewLabel_1_1);
		
		btnGuardar = new JButton("Guardar Modificaciones");
		btnGuardar.addActionListener(this);
		btnGuardar.setBounds(58, 603, 179, 20);
		contentPane.add(btnGuardar);
		
		btnEliminar = new JButton("Eliminar Empleado");
		btnEliminar.setBounds(58, 633, 179, 20);
		contentPane.add(btnEliminar);
		
		btnExportPDF = new JButton("Exportar PDF");
		btnExportPDF.setBounds(10, 676, 117, 20);
		contentPane.add(btnExportPDF);
		
		btnExportExcel = new JButton("Exportar Excel");
		btnExportExcel.setBounds(150, 676, 117, 20);
		contentPane.add(btnExportExcel);
		
		checkAll = new JCheckBox("Todo");
		checkAll.setBackground(new Color(0, 128, 128));
		checkAll.setBounds(228, 430, 62, 20);
		contentPane.add(checkAll);
		
		radioEmp = new JRadioButton("Empleado");
		radioEmp.setSelected(true);
		radioEmp.setBackground(new Color(0, 128, 128));
		radioEmp.setBounds(190, 516, 102, 20);
		buttonGroup.add(radioEmp);
		contentPane.add(radioEmp);
		
		radioTemp = new JRadioButton("Temporal");
		radioTemp.setBackground(new Color(0, 128, 128));
		radioTemp.setBounds(190, 538, 102, 20);
		buttonGroup.add(radioTemp);
		contentPane.add(radioTemp);
		
	}
	
	class CustomColorRenderer extends DefaultListCellRenderer {

        private final Color MI_COLOR_DE_FONDO = new Color(0, 128, 128);

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            setBackground(MI_COLOR_DE_FONDO);
            
            setForeground(Color.BLACK);

            return this;
        }
    }


	private void cargarTablaEmpleados() {
		
		empList = null;

		modeloP = new DefaultTableModel();
		modeloP.addColumn("ID");
		modeloP.addColumn("Nombre");
		modeloP.addColumn("Apellido");
		modeloP.addColumn("Apellido 2");
		modeloP.addColumn("DNI");
		modeloP.addColumn("Género");
		modeloP.addColumn("Puesto");
		modeloP.addColumn("Email");
		modeloP.addColumn("Nivel de Acceso");
		empList = new ArrayList<Empleado>();
		String sql = "SELECT * FROM empleados";
		
		try {
			ConexionDB conexion = new ConexionDB();
			empList = conexion.obtenerEmpleados(sql);
			for (Empleado emp : empList) {
				modeloP.addRow(emp.crear());
			}
			
			tablaPrincipal.setModel(modeloP);
			tablaPrincipal.getColumnModel().getColumn(0).setMaxWidth(0);
			tablaPrincipal.getColumnModel().getColumn(0).setMinWidth(0);
			tablaPrincipal.getColumnModel().getColumn(0).setPreferredWidth(0);
			
		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		
	}
	
	private void cargarTablaTemporales() {
		
		tempList = null;
		
		modeloP = new DefaultTableModel();
		modeloP.addColumn("ID");
		modeloP.addColumn("Nombre");
		modeloP.addColumn("Apellido");
		modeloP.addColumn("Apellido 2");
		modeloP.addColumn("DNI");
		modeloP.addColumn("Motivo");
		modeloP.addColumn("Foto");
		modeloP.addColumn("Foto");
		modeloP.addColumn("Foto");
		modeloP.addColumn("Fecha Expiración");
		tempList = new ArrayList<VisitaTemporal>();
		String sql = "SELECT * FROM usuarios_temporales";
		
		try {
			ConexionDB conexion = new ConexionDB();
			tempList = conexion.obtenerTemporal(sql);
			for (VisitaTemporal temp : tempList) {
				modeloP.addRow(temp.crear());
			}
			
			tablaPrincipal.setModel(modeloP);
			tablaPrincipal.getColumnModel().getColumn(0).setMaxWidth(0);
			tablaPrincipal.getColumnModel().getColumn(0).setMinWidth(0);
			tablaPrincipal.getColumnModel().getColumn(0).setPreferredWidth(0);
			tablaPrincipal.getColumnModel().getColumn(6).setMaxWidth(0);
			tablaPrincipal.getColumnModel().getColumn(6).setMinWidth(0);
			tablaPrincipal.getColumnModel().getColumn(6).setPreferredWidth(0);
			tablaPrincipal.getColumnModel().getColumn(7).setMaxWidth(0);
			tablaPrincipal.getColumnModel().getColumn(7).setMinWidth(0);
			tablaPrincipal.getColumnModel().getColumn(7).setPreferredWidth(0);
			tablaPrincipal.getColumnModel().getColumn(8).setMaxWidth(0);
			tablaPrincipal.getColumnModel().getColumn(8).setMinWidth(0);
			tablaPrincipal.getColumnModel().getColumn(8).setPreferredWidth(0);
			
			
		
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
		if (btnGuardar == e.getSource()) {
			modificarEmpleado(tipoEntidad);
		}
		
		if(btnBuscar == e.getSource()) {
			if (checkAll.isSelected()) {
				if (radioEmp.isSelected()) {
					cargarTablaEmpleados();
				} else if (radioTemp.isSelected()) {
					cargarTablaTemporales();
				}
			} else {
				// Lógica de búsqueda avanzada aquí
				JOptionPane.showMessageDialog(this, "Funcionalidad de búsqueda avanzada no implementada aún.");
			}
		}
		
		
	}



	private void modificarEmpleado(String tipo) {
		
		if (tablaPrincipal.isEditing()) {
	        tablaPrincipal.getCellEditor().stopCellEditing();
	    }

	    // --- 2. Obtener la fila seleccionada ---
	    int filaSeleccionada = tablaPrincipal.getSelectedRow();

	    if (filaSeleccionada == -1) {
	        JOptionPane.showMessageDialog(this, "Por favor, selecciona una fila para modificar.");
	        return;
	    }
		
	    try {
	    	
	    	ConexionDB conexion = new ConexionDB();
			
			if (tipo.equals("empleado")) {
				int idEmpleado = (int) tablaPrincipal.getValueAt(filaSeleccionada, 0);
				String name = (String) tablaPrincipal.getValueAt(filaSeleccionada, 1);
				String lastName1 = (String) tablaPrincipal.getValueAt(filaSeleccionada, 2);
				String lastName2 = (String) tablaPrincipal.getValueAt(filaSeleccionada, 3);
				String dni = (String) tablaPrincipal.getValueAt(filaSeleccionada, 4);
				String genero = (String) tablaPrincipal.getValueAt(filaSeleccionada, 5);
				String puesto = (String) tablaPrincipal.getValueAt(filaSeleccionada, 6);
				String email = (String) tablaPrincipal.getValueAt(filaSeleccionada, 7);
				int nivelAcceso = (int) tablaPrincipal.getValueAt(filaSeleccionada, 8);
					
				conexion.actualizarEmpleado(idEmpleado, name, lastName1, lastName2, dni, genero, puesto, email, nivelAcceso);
				
			} else if (tipo.equals("temporal")) {
				for (int i = 0; i < tablaPrincipal.getRowCount(); i++) {
					int idVisita = (int) tablaPrincipal.getValueAt(i, 0);
					String name = (String) tablaPrincipal.getValueAt(i, 1);
					String lastName1 = (String) tablaPrincipal.getValueAt(i, 2);
					String lastName2 = (String) tablaPrincipal.getValueAt(i, 3);
					String dni = (String) tablaPrincipal.getValueAt(i, 4);
					String motivo = (String) tablaPrincipal.getValueAt(i, 5);
					
					conexion.actualizarTemporal(idVisita, name, lastName1, lastName2, dni, motivo);
				}
			}
			
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, "Error al obtener los datos de la tabla: " + e.getMessage());
	        e.printStackTrace();
	    }
		
	}
}
