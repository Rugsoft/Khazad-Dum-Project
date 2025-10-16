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

import javax.swing.JPasswordField;

public class LoginForm extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textUser;
	private JButton btnLogin;
	private final JButton btnRegister = new JButton("Registrar");
	private final JPasswordField passwordField = new JPasswordField();
	private RegisterForm dialog = new RegisterForm();

	
	public LoginForm() {
		setBounds(100, 100, 314, 390);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(64, 128, 128));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		visualElements();
		
	}

	private void visualElements() {
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LoginForm.class.getResource("/kazaddum/images/LogoLogin-removebg-125px.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(75, 11, 137, 125);
		contentPanel.add(lblNewLabel);
		
		textUser = new JTextField();
		textUser.setBackground(new Color(64, 128, 128));
		textUser.setBorder(new TitledBorder(null, "Usuario", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textUser.setBounds(75, 155, 137, 36);
		contentPanel.add(textUser);
		textUser.setColumns(10);
		
		btnLogin = new JButton("");
		btnLogin.setBorder(null);
		btnLogin.setBackground(new Color(64, 128, 128));
		btnLogin.setIcon(new ImageIcon(LoginForm.class.getResource("/kazaddum/images/Boton3-recortado-removebg-170px.png")));
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogin.setBounds(48, 254, 194, 86);
		btnLogin.addActionListener(this);
		contentPanel.add(btnLogin);
		
		btnRegister.setBounds(205, 5, 89, 23);
		btnRegister.addActionListener(this);
		contentPanel.add(btnRegister);
		passwordField.setBackground(new Color(64, 128, 128));
		passwordField.setBorder(new TitledBorder(null, "Contrase\u00F1a", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		passwordField.setBounds(75, 202, 137, 36);
		
		contentPanel.add(passwordField);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (btnRegister == e.getSource()) {
			
			registerForm();
		}
		if (btnLogin == e.getSource()) {
			
			loginCheck();
		}
		
	}

	private void loginCheck() {
		
		String user = textUser.getText();
		String pass = new String(passwordField.getPassword());
		
		if (!user.trim().isEmpty() && !pass.trim().isEmpty()) {
			
			String sql = "SELECT nivel FROM login_usuarios WHERE username = ? AND password = ?";
			String nivel = ConexionDB.comprobarLogin(sql, user, pass);
			if (nivel != null) {
				
				switch (nivel) {
				case "Gandalf":
					
					MainWindowBalrog admin = new MainWindowBalrog(user, nivel);
					admin.setVisible(true);
					this.dispose();
					break;
					
				case "Balrog":
					
					MainWindowBalrog seguridad = new MainWindowBalrog(user, nivel);
					seguridad.setVisible(true);
					this.dispose();
					break;	
					
				case "Goblin":
					
					MainWindowBalrog consultor = new MainWindowBalrog(user, nivel);
					consultor.setVisible(true);
					this.dispose();
					break;
					
				} 
				
			} else {
				JOptionPane.showMessageDialog(null, "Usuario o contrase\u00F1a incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
				
			}
	        

		} else {
			
			JOptionPane.showMessageDialog(null, "Rellena los dos campos", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	private void registerForm() {
		
		dialog.setVisible(true);
		
	}
}