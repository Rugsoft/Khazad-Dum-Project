package khazaddum.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import khazaddum.modelo.UserLogin;
import khazaddum.operaciones.ConexionDB;

import javax.swing.border.EtchedBorder;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import java.awt.Cursor;

/**
 * Diálogo para registrar nuevos usuarios/administradores del sistema.
 * <p>
 * Permite introducir los datos del usuario, seleccionar su rol y almacenar
 * las credenciales en la base de datos mediante {@link ConexionDB}.
 * </p>
 */
public class RegisterForm extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTextField textName;
	private JTextField textApellido;
	private JTextField textUser;
	private JPasswordField passwordField;
	private JTextField textEmail;
	private JComboBox<String> comboBox;
	private JButton btnRegister;
	private List<UserLogin> listaUsuarios = new ArrayList<>();

	/**
	 * Construye el formulario de registro y configura la interfaz gráfica.
	 */
	public RegisterForm() {
		setTitle("Registro de Usuarios/Admins");
		
		setBounds(100, 100, 392, 434);
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(64, 128, 128));
		getContentPane().setLayout(null);
		
		visualElements();
		
	}
	
	/**
	 * Inicializa y coloca los elementos visuales en el diálogo.
	 */
	private void visualElements() {
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(RegisterForm.class.getResource("/kazaddum/images/IconoRegistro-removebg-150px.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(134, 11, 112, 107);
		getContentPane().add(lblNewLabel);
		
		textName = new JTextField();
		textName.setBackground(new Color(64, 128, 128));
		textName.setBorder(new TitledBorder(null, "Nombre", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textName.setBounds(41, 138, 139, 36);
		getContentPane().add(textName);
		textName.setColumns(10);
		
		textApellido = new JTextField();
		textApellido.setColumns(10);
		textApellido.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Apellido", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textApellido.setBackground(new Color(64, 128, 128));
		textApellido.setBounds(204, 138, 139, 36);
		getContentPane().add(textApellido);
		
		textUser = new JTextField();
		textUser.setColumns(10);
		textUser.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Usuario", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textUser.setBackground(new Color(64, 128, 128));
		textUser.setBounds(41, 185, 139, 36);
		getContentPane().add(textUser);
		
		passwordField = new JPasswordField();
		passwordField.setBackground(new Color(64, 128, 128));
		passwordField.setBorder(new TitledBorder(null, "Contrase\u00F1a", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		passwordField.setBounds(204, 185, 139, 36);
		getContentPane().add(passwordField);
		
		textEmail = new JTextField();
		textEmail.setColumns(10);
		textEmail.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "E-mail", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textEmail.setBackground(new Color(64, 128, 128));
		textEmail.setBounds(41, 232, 139, 36);
		getContentPane().add(textEmail);
		
		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Seleccione", "Goblin", "Balrog", "Gandalf"}));
		comboBox.setBackground(new Color(64, 128, 128));
		comboBox.setBorder(new TitledBorder(null, "Nivel Seg.", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		comboBox.setBounds(204, 232, 139, 42);
		comboBox.setRenderer(new CustomColorRenderer());
		getContentPane().add(comboBox);
		
		btnRegister = new JButton("");
		btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRegister.setBorder(null);
		btnRegister.setBackground(new Color(64, 128, 128));
		btnRegister.setIcon(new ImageIcon(RegisterForm.class.getResource("/kazaddum/images/BotonRegistrar-removebg-100px.png")));
		btnRegister.setBounds(126, 307, 133, 54);
		btnRegister.addActionListener(this);
		getContentPane().add(btnRegister);
		
	}

	class CustomColorRenderer extends DefaultListCellRenderer {

        // Aquí defines tu color usando los valores RGB de la imagen
        private final Color MI_COLOR_DE_FONDO = new Color(64, 128, 128);

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            
            // Llama al método original para que se encargue del texto y la selección
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // Establecemos nuestro color de fondo personalizado
            setBackground(MI_COLOR_DE_FONDO);
            
            // Para que el texto se lea bien, lo ponemos en blanco
            setForeground(Color.BLACK);

            return this;
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (btnRegister == e.getSource()) {
			
			registrarUsuario();
		}
		
	}

	/**
	 * Valida los campos del formulario y registra un nuevo usuario en la base de datos.
	 * Usa {@link ConexionDB#añadirUsuariosLogin(String, Object[])} para insertar.
	 */
	private void registrarUsuario() {
		
		// Validar campos vacíos correctamente
	    if (textName.getText().trim().isEmpty() ||
	        textApellido.getText().trim().isEmpty() ||
	        textUser.getText().trim().isEmpty() ||
	        passwordField.getPassword().length == 0 ||
	        textEmail.getText().trim().isEmpty() ||
	        comboBox.getSelectedIndex() == 0) { // 0 es "Seleccione"

	        JOptionPane.showMessageDialog(null, "Rellena todos los campos y selecciona una opcion del desplegable", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	        
	    } else {
	    	
	    	String name = textName.getText();
	    	String apellido = textApellido.getText();
	    	String usuario = textUser.getText();
	    	String password = new String(passwordField.getPassword());
	    	String email = textEmail.getText();
	    	String nivel = comboBox.getSelectedItem().toString();
	    	
	    	UserLogin nuevoUsuario = new UserLogin(name, apellido, usuario, password, email, nivel);
	    	listaUsuarios.add(nuevoUsuario);
	    	String sql = "INSERT INTO login_usuarios (nombre, apellido, username, password, email, nivel) VALUES (?, ?, ?, ?, ?, ?)";
	    	
	    	boolean resultConection = ConexionDB.añadirUsuariosLogin(sql, nuevoUsuario.crear());
	    	
	    	if (resultConection) {
	    		limpiarCampos();
	    	}
	    	
	    	
	    }
		
	}
	
	/**
	 * Limpia los campos del formulario.
	 */
	public void limpiarCampos(){
		
		textName.setText("");
		textApellido.setText("");
		textUser.setText("");
		passwordField.setText("");
		textEmail.setText("");
		comboBox.setSelectedIndex(0);
	
	}
}