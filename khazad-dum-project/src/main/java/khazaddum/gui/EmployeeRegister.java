package khazaddum.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import khazaddum.modelo.Empleado;
import khazaddum.modelo.VisitaTemporal;
import khazaddum.operaciones.ComunicacionSerie;
import khazaddum.operaciones.ComunicacionSerie.SerialDataCallback;
import khazaddum.operaciones.ConexionDB;

import javax.swing.border.EtchedBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class EmployeeRegister extends JDialog implements ActionListener, SerialDataCallback {

	private static final long serialVersionUID = 1L;
	private JTextField textName;
	private JTextField textLasName1;
	private JTextField textLastName2;
	private JTextField textDNI;
	private JButton btnPicture;
	JFileChooser filechoser = new JFileChooser();
	File selectedPicture;
	private JButton btnRegister;
	private JComboBox<String> comboGender;
	private JTextField txtRuraFoto;
	private ComunicacionSerie miConexion;
    private String mensajeDeArduino = "";
    private JLabel lblNewLabel;
    private JTextField textRole;
    private JTextField textEmail;
    private JComboBox<String> comboNivel;

	
	public EmployeeRegister() {
		setTitle("Registro de Empleado");
		getContentPane().setBackground(new Color(0, 128, 128));
		getContentPane().setLayout(null);
		setLocationRelativeTo(null); // Centra la ventana
		
		elementosVisuales();
		
	}
	
	private void elementosVisuales() {
		textName = new JTextField();
		textName.setColumns(10);
		textName.setBorder(new TitledBorder(null, "Nombre", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textName.setBackground(new Color(0, 128, 128));
		textName.setBounds(10, 134, 139, 36);
		getContentPane().add(textName);
		
		textLasName1 = new JTextField();
		textLasName1.setColumns(10);
		textLasName1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Primer Apellido", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textLasName1.setBackground(new Color(0, 128, 128));
		textLasName1.setBounds(159, 134, 139, 36);
		getContentPane().add(textLasName1);
		
		textLastName2 = new JTextField();
		textLastName2.setColumns(10);
		textLastName2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Segundo Apellido", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textLastName2.setBackground(new Color(0, 128, 128));
		textLastName2.setBounds(308, 134, 139, 36);
		getContentPane().add(textLastName2);
		
		textDNI = new JTextField();
		textDNI.setColumns(10);
		textDNI.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "DNI", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textDNI.setBackground(new Color(0, 128, 128));
		textDNI.setBounds(10, 180, 139, 36);
		getContentPane().add(textDNI);
		
		btnPicture = new JButton("Añadir Foto");
		btnPicture.addActionListener(this);
		btnPicture.setBounds(10, 290, 139, 29);
		getContentPane().add(btnPicture);
		
		comboGender = new JComboBox<String>();
		comboGender.setModel(new DefaultComboBoxModel<String>(new String[] {"Seleccione", "Masculino", "Femenino", "No Binario"}));
		comboGender.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Genero", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		comboGender.setBackground(new Color(64, 128, 128));
		comboGender.setRenderer(new CustomColorRenderer());
		comboGender.setBounds(10, 226, 139, 42);
		getContentPane().add(comboGender);
		
		btnRegister = new JButton("");
		btnRegister.setIcon(new ImageIcon(TemporaryUserRegister.class.getResource("/kazaddum/images/BotonRegistrar-removebg-100px.png")));
		btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRegister.setBorder(null);
		btnRegister.setBackground(new Color(0, 128, 128));
		btnRegister.setBounds(165, 336, 133, 54);
		btnRegister.addActionListener(this);
		getContentPane().add(btnRegister);
		
		txtRuraFoto = new JTextField();
		txtRuraFoto.setText("Nada seleccionado..");
		txtRuraFoto.setEditable(false);
		txtRuraFoto.setBackground(new Color(0, 128, 128));
		txtRuraFoto.setBorder(new TitledBorder(null, "Foto seleccionada", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtRuraFoto.setBounds(159, 282, 139, 36);
		getContentPane().add(txtRuraFoto);
		txtRuraFoto.setColumns(10);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(EmployeeRegister.class.getResource("/kazaddum/images/IconoRegistroEmpleados-removebg-250px.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(85, -2, 300, 132);
		getContentPane().add(lblNewLabel);
		
		textRole = new JTextField();
		textRole.setColumns(10);
		textRole.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Puesto", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textRole.setBackground(new Color(0, 128, 128));
		textRole.setBounds(308, 180, 139, 36);
		getContentPane().add(textRole);
		
		textEmail = new JTextField();
		textEmail.setColumns(10);
		textEmail.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Email", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textEmail.setBackground(new Color(0, 128, 128));
		textEmail.setBounds(159, 180, 139, 36);
		getContentPane().add(textEmail);
		
		comboNivel = new JComboBox<String>();
		comboNivel.setModel(new DefaultComboBoxModel<String>(new String[] {"Seleccione", "1", "2", "3"}));
		comboNivel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Nivel Acc.", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		comboNivel.setBackground(new Color(64, 128, 128));
		comboNivel.setRenderer(new CustomColorRenderer());
		comboNivel.setBounds(159, 226, 139, 42);
		getContentPane().add(comboNivel);
		setBounds(100, 100, 476, 432);
		
	}

	class CustomColorRenderer extends DefaultListCellRenderer {

        private final Color MI_COLOR_DE_FONDO = new Color(64, 128, 128);

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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(btnPicture == e.getSource()) {
			
			cargarImagen();
		}
		if (btnRegister == e.getSource()) {
			
					
			// Validación simple
			if ( !textName.getText().isEmpty() && !textLasName1.getText().isEmpty() && !textLastName2.getText().isEmpty() && !textDNI.getText().isEmpty() && 
				 !textEmail.getText().isEmpty() && !textRole.getText().isEmpty() && comboGender.getSelectedIndex() != 0 && comboNivel.getSelectedIndex() != 0 && selectedPicture != null ) {
				
				// 1. Recoger y validar los datos actuales
				String nombre = textName.getText();
				String apellido1 = textLasName1.getText();
				String apellido2 = textLastName2.getText();
				String dni = textDNI.getText();
				String email = textEmail.getText();
				String role = textRole.getText();
				String genero = (String) comboGender.getSelectedItem();
				int nivelAcceso = Integer.parseInt((String) comboNivel.getSelectedItem());
				// 2. Abrir el diálogo de lectura RFID
				
				miConexion = new ComunicacionSerie();
				miConexion.setSerialDataCallback(this);
				miConexion.conectar();
				
				JOptionPane.showMessageDialog(this, "Por favor, acerque el tag RFID al lector.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
				
				// 3. Recoger el resultado del diálogo RFID
				String codigoTag = mensajeDeArduino;
							
				// 4. Continuar solo si se leyó un tag
				if (codigoTag != null && !codigoTag.isEmpty()) {
					// 5. Llamar al registro CON TODOS los datos
					Empleado emp = new Empleado(nombre, apellido1, apellido2, dni, genero, role, email, nivelAcceso, selectedPicture, codigoTag);
					registerEmployee(emp);
					miConexion.desconectar();
					this.dispose(); // Cierra la ventana de registro
				} else {
					// El usuario cerró el diálogo RFID o no se leyó nada
					JOptionPane.showMessageDialog(this, "Registro cancelado. No se leyó ningún tag RFID.", "Cancelado", JOptionPane.WARNING_MESSAGE);
				}
				
			} else {
				JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos y seleccione una foto.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
				return;
			}		
			
		}
	}

	private void registerEmployee(Empleado emp) {

				
		try {
			
			ConexionDB.añadirEmpleado(emp.crear()); 
		} catch (FileNotFoundException e) {
			System.out.println("No se encontro el fichero: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al guardar la imagen: " + e.getMessage(), "Error de Fichero", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			// Captura genérica para otros errores
			System.out.println("Error al registrar: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al registrar en la base de datos: " + e.getMessage(), "Error de DB", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarImagen() {
		
		FileNameExtensionFilter filtro = new FileNameExtensionFilter(
			    "Archivos de Imagen (JPG, PNG, GIF)", // Descripción que ve el usuario
			    "jpg", "jpeg", "png", "gif"           // Extensiones permitidas
			);
		filechoser.setFileFilter(filtro);
		
		int result = filechoser.showOpenDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			
		    selectedPicture = filechoser.getSelectedFile();
		    txtRuraFoto.setText(selectedPicture.getAbsolutePath());
		 
		}	
	}

	@Override
	public void onDatoRecibido(String dato) {
		mensajeDeArduino = dato;
		
	}
}
