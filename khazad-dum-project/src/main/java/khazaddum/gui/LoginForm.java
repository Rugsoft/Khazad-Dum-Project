package khazaddum.gui;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import khazaddum.operaciones.ConexionDB;

import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPasswordField;

/**
 * Representa el cuadro de diálogo de inicio de sesión de la aplicación.
 * Permite a los usuarios ingresar sus credenciales (usuario y contraseña)
 * para acceder al sistema o navegar al formulario de registro.
 * <p>
 * Hereda de {@link JDialog} y implementa {@link ActionListener} para manejar
 * los eventos de los botones.
 * </p>
 */
public class LoginForm extends JDialog implements ActionListener {

	/** Versión de serialización estándar para componentes Swing. */
	private static final long serialVersionUID = 1L;
	
	/** Panel principal que contiene todos los componentes de la GUI. */
	private final JPanel contentPanel = new JPanel();
	
	/** Logger para registrar eventos y errores. */
	private static final Logger logger = LoggerFactory.getLogger(LoginForm.class);
	
	/** Campo de texto para ingresar el nombre de usuario. */
	private JTextField textUser;
	
	/** Botón para iniciar el proceso de verificación de inicio de sesión. */
	private JButton btnLogin;
	
	/** Botón para abrir el diálogo de registro de nuevos usuarios. */
	private final JButton btnRegister = new JButton("Registrar");
	
	/** Campo de texto seguro para ingresar la contraseña. */
	private final JPasswordField passwordField = new JPasswordField();
	
	/** Instancia del formulario de registro ({@link RegisterForm}) que se puede mostrar. */
	private RegisterForm dialog = new RegisterForm();

	/**
	 * Constructor principal de la clase LoginForm.
	 * Configura las propiedades iniciales del diálogo (título, tamaño, posición)
	 * y llama al método {@link #visualElements()} para inicializar los componentes visuales.
	 */
	public LoginForm() {
		setTitle("Inciar Sesion");
		setBounds(100, 100, 314, 390);
		setLocationRelativeTo(null); // Centra la ventana
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(64, 128, 128));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null); // Usamos layout absoluto
		
		visualElements();
	}

	/**
	 * Método privado para inicializar y configurar todos los componentes visuales
	 * (GUI) del formulario de inicio de sesión.
	 */
	private void visualElements() {
		
		// Etiqueta para mostrar el logo
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LoginForm.class.getResource("/kazaddum/images/LogoLogin-removebg-125px.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(75, 11, 137, 125);
		contentPanel.add(lblNewLabel);
		
		// Campo de texto para el usuario
		textUser = new JTextField();
		textUser.setBackground(new Color(64, 128, 128));
		textUser.setBorder(new TitledBorder(null, "Usuario", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textUser.setBounds(75, 155, 137, 36);
		contentPanel.add(textUser);
		textUser.setColumns(10);
		
		// Botón de Login (con icono)
		btnLogin = new JButton("");
		btnLogin.setBorder(null);
		btnLogin.setBackground(new Color(64, 128, 128));
		btnLogin.setIcon(new ImageIcon(LoginForm.class.getResource("/kazaddum/images/Boton3-recortado-removebg-170px.png")));
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogin.setBounds(48, 254, 194, 86);
		btnLogin.addActionListener(this); // Asigna este objeto como listener
		contentPanel.add(btnLogin);
		
		// Botón de Registro
		btnRegister.setBounds(205, 5, 89, 23);
		btnRegister.addActionListener(this); // Asigna este objeto como listener
		contentPanel.add(btnRegister);
		
		// Campo de contraseña
		passwordField.setBackground(new Color(64, 128, 128));
		passwordField.setBorder(new TitledBorder(null, "Contrase\u00F1a", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		passwordField.setBounds(75, 202, 137, 36);
		contentPanel.add(passwordField);
	}

	/**
	 * Manejador central de eventos de acción para esta clase.
	 * Captura los clics en los botones {@link #btnLogin} y {@link #btnRegister}.
	 * @param e El evento de acción que se disparó (generalmente un clic de botón).
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Si se pulsó el botón de Registrar
		if (btnRegister == e.getSource()) {
			registerForm();
		}
		
		// Si se pulsó el botón de Login
		if (btnLogin == e.getSource()) {
			loginCheck();
		}
	}

	/**
	 * Procesa el intento de inicio de sesión.
	 * Valida los campos y llama a {@link ConexionDB#comprobarLogin(String, String, String)}.
	 */
	private void loginCheck() {
		
		String user = textUser.getText();
		String pass = new String(passwordField.getPassword());
		
		// Verifica que los campos no estén vacíos (después de quitar espacios)
		if (!user.trim().isEmpty() && !pass.trim().isEmpty()) {
			
			String sql = "SELECT nivel FROM login_usuarios WHERE username = ? AND password = ?";
			// Comprueba el login usando la clase de conexión
			String nivel = ConexionDB.comprobarLogin(sql, user, pass);
			
			if (nivel != null) {
				// Si el nivel no es nulo, el login fue exitoso
				
				// Abre la ventana principal según el nivel de usuario
				switch (nivel) {
				case "Gandalf":
					MainWindowGandalf ventanaGandalf = new MainWindowGandalf(user, nivel);
					ventanaGandalf.setVisible(true);
					logger.info("Usuario {} ha iniciado sesi\u00F3n con nivel {}", user, nivel);
					this.dispose(); // Cierra la ventana de login
					break;
				case "Balrog":
				case "Goblin":
					MainWindowBalrog ventanaBalrog = new MainWindowBalrog(user, nivel);
					ventanaBalrog.setVisible(true);
					logger.info("Usuario {} ha iniciado sesi\u00F3n con nivel {}", user, nivel);
					this.dispose(); // Cierra la ventana de login
					break;
				} 
				
			} else {
				// Credenciales incorrectas
				logger.warn("Intento de login fallido para el usuario: {}", user);
				JOptionPane.showMessageDialog(null, "Usuario o contrase\u00F1a incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
			}
	        
		} else {
			// Campos vacíos
			JOptionPane.showMessageDialog(null, "Rellena los dos campos", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Muestra el formulario de registro ({@link #dialog}) haciéndolo visible.
	 */
	private void registerForm() {
		dialog.setVisible(true);
	}
}