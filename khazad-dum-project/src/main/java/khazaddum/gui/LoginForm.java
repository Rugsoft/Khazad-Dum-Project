package khazaddum.gui;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LoginForm dialog = new LoginForm();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
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
		
	}

	private void registerForm() {
		
		dialog.setVisible(true);
		
	}
}