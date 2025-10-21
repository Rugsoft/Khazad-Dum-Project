package khazaddum.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import khazaddum.modelo.VisitaTemporal;
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

public class TemporaryUserRegister extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTextField textName;
	private JTextField textLasName1;
	private JTextField textLastName2;
	private JTextField textDNI;
	private JButton btnPicture;
	JFileChooser filechoser = new JFileChooser();
	File selectedPicture;
	private JTextArea textReason;
	private JButton btnRegisterTemp;
	private JComboBox<String> comboTime;
	private JTextField txtRuraFoto;

	
	public TemporaryUserRegister() {
		setTitle("Registro Temporal");
		getContentPane().setBackground(new Color(0, 128, 128));
		getContentPane().setLayout(null);
		
		elementosVisuales();
		
	}
	
	private void elementosVisuales() {
		textName = new JTextField();
		textName.setColumns(10);
		textName.setBorder(new TitledBorder(null, "Nombre", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textName.setBackground(new Color(0, 128, 128));
		textName.setBounds(10, 118, 139, 36);
		getContentPane().add(textName);
		
		textLasName1 = new JTextField();
		textLasName1.setColumns(10);
		textLasName1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Primer Apellido", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textLasName1.setBackground(new Color(0, 128, 128));
		textLasName1.setBounds(159, 118, 139, 36);
		getContentPane().add(textLasName1);
		
		textLastName2 = new JTextField();
		textLastName2.setColumns(10);
		textLastName2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Segundo Apellido", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textLastName2.setBackground(new Color(0, 128, 128));
		textLastName2.setBounds(10, 164, 139, 36);
		getContentPane().add(textLastName2);
		
		textDNI = new JTextField();
		textDNI.setColumns(10);
		textDNI.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "DNI", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textDNI.setBackground(new Color(0, 128, 128));
		textDNI.setBounds(159, 164, 139, 36);
		getContentPane().add(textDNI);
		
		btnPicture = new JButton("Añadir Foto");
		btnPicture.addActionListener(this);
		btnPicture.setBounds(10, 308, 288, 29);
		getContentPane().add(btnPicture);
		
		comboTime = new JComboBox<String>();
		comboTime.setModel(new DefaultComboBoxModel<String>(new String[] {"Seleccione", "4", "8", "12", "24"}));
		comboTime.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Tiempo Permiso (h)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		comboTime.setBackground(new Color(64, 128, 128));
		comboTime.setRenderer(new CustomColorRenderer());
		comboTime.setBounds(10, 347, 139, 42);
		getContentPane().add(comboTime);
		
		textReason = new JTextArea();
		textReason.setBackground(new Color(0, 128, 128));
		textReason.setBorder(new TitledBorder(null, "Motivo visita", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textReason.setLineWrap(true);
		textReason.setBounds(10, 210, 288, 88);
		getContentPane().add(textReason);
		
		btnRegisterTemp = new JButton("");
		btnRegisterTemp.setIcon(new ImageIcon(TemporaryUserRegister.class.getResource("/kazaddum/images/BotonRegistrar-removebg-100px.png")));
		btnRegisterTemp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRegisterTemp.setBorder(null);
		btnRegisterTemp.setBackground(new Color(0, 128, 128));
		btnRegisterTemp.setBounds(93, 414, 133, 54);
		btnRegisterTemp.addActionListener(this);
		getContentPane().add(btnRegisterTemp);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(TemporaryUserRegister.class.getResource("/kazaddum/images/IconoRegistroTemporal_-removebg-200px.png")));
		lblNewLabel.setBounds(57, 16, 186, 81);
		getContentPane().add(lblNewLabel);
		
		txtRuraFoto = new JTextField();
		txtRuraFoto.setText("Nada seleccionado..");
		txtRuraFoto.setEditable(false);
		txtRuraFoto.setBackground(new Color(0, 128, 128));
		txtRuraFoto.setBorder(new TitledBorder(null, "Foto seleccionada", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtRuraFoto.setBounds(159, 347, 139, 36);
		getContentPane().add(txtRuraFoto);
		txtRuraFoto.setColumns(10);
		setBounds(100, 100, 334, 515);
		
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
		if (btnRegisterTemp == e.getSource()) {
			
			registerTempUser();
		}
		
	}

	private void registerTempUser() {

		String nombre = textName.getText();
		String apellido1 = textLasName1.getText();
		String apellido2 = textLastName2.getText();
		String dni = textDNI.getText();
		String motivo = textReason.getText();
		int horas = Integer.parseInt(comboTime.getSelectedItem().toString());
		
		VisitaTemporal tempUser = new VisitaTemporal(nombre, apellido1, apellido2, dni, motivo, selectedPicture, horas);
		
		try {
			ConexionDB.añadirUsuarioTemporal(tempUser.crear());
		} catch (FileNotFoundException e) {
			System.out.println("No se encontro el fichero: " + e.getMessage());
			e.printStackTrace();
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
	
	
	
}
