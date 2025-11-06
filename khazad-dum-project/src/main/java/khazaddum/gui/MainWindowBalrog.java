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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import khazaddum.modelo.Empleado;
import khazaddum.modelo.ResultadoIdentificacion;
import khazaddum.operaciones.ComunicacionSerie.SerialDataCallback;
import khazaddum.operaciones.ComunicacionSerie;
import khazaddum.operaciones.ConexionDB;

import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * Ventana principal para usuarios de nivel Balrog/Goblin.
 * <p>
 * Proporciona la interfaz para recibir lecturas RFID en tiempo real,
 * mostrar información breve del usuario detectado y registrar entradas/salidas.
 * Implementa {@link SerialDataCallback} para recibir datos desde la clase
 * de comunicación serie y {@link ActionListener} para manejar eventos de GUI.
 * </p>
 */
public class MainWindowBalrog extends JFrame implements ActionListener, SerialDataCallback {

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
	private ComunicacionSerie miConexion;
    private String mensajeDeArduino = "";
    private javax.swing.Timer clearDataTimer;
    private JButton btnBackLogin;

    /**
     * Construye la ventana principal para el usuario con el nombre y nivel dados.
     * Inicia la comunicación serie y registra el callback para recibir tags.
     *
     * @param user  nombre del usuario conectado
     * @param nivel nivel de acceso del usuario
     */
	public MainWindowBalrog(String user, String nivel) {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setResizable(false);
		setTitle("Khazzad-Dûm Pro");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1044, 588);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		visualElements(nivel);
		saludo(user, nivel);
		miConexion = new ComunicacionSerie();
		miConexion.setSerialDataCallback(this);
		miConexion.conectar();
		
	}

	/**
	 * Actualiza el saludo mostrado en la cabecera con el usuario y su nivel.
	 *
	 * @param user  nombre del usuario
	 * @param nivel nivel de acceso del usuario
	 */
	private void saludo(String user, String nivel) {
		
		lblSaludo.setText("Bienvenido " + user + " - Nivel: " + nivel);
		
	}

	/**
	 * Crea y posiciona los elementos visuales de la interfaz.
	 *
	 * @param nivel indicador de nivel para ajustar ciertos controles.
	 */
	private void visualElements(String nivel) {
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
				if (nivel.equals("Gandalf")) {
					dispose();
				} else {
					System.exit(0);
				}
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
		textName.setBounds(195, 59, 148, 40);
		panel_1.add(textName);
		textName.setColumns(10);
		
		textLastName1 = new JTextField();
		textLastName1.setEditable(false);
		textLastName1.setColumns(10);
		textLastName1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "1er Apellido", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textLastName1.setBackground(new Color(128, 128, 255));
		textLastName1.setBounds(353, 59, 148, 40);
		panel_1.add(textLastName1);
		
		textLasName2 = new JTextField();
		textLasName2.setEditable(false);
		textLasName2.setColumns(10);
		textLasName2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "2do Apellido", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textLasName2.setBackground(new Color(128, 128, 255));
		textLasName2.setBounds(511, 59, 148, 40);
		panel_1.add(textLasName2);
		
		textDNI = new JTextField();
		textDNI.setEditable(false);
		textDNI.setColumns(10);
		textDNI.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "DNI", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textDNI.setBackground(new Color(128, 128, 255));
		textDNI.setBounds(195, 103, 148, 40);
		panel_1.add(textDNI);
		
		textEmail = new JTextField();
		textEmail.setEditable(false);
		textEmail.setColumns(10);
		textEmail.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Email", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textEmail.setBackground(new Color(128, 128, 255));
		textEmail.setBounds(353, 103, 148, 40);
		panel_1.add(textEmail);
		
		textGender = new JTextField();
		textGender.setEditable(false);
		textGender.setColumns(10);
		textGender.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Genero", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textGender.setBackground(new Color(128, 128, 255));
		textGender.setBounds(511, 103, 148, 40);
		panel_1.add(textGender);
		
		textRole = new JTextField();
		textRole.setEditable(false);
		textRole.setColumns(10);
		textRole.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Puesto", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textRole.setBackground(new Color(128, 128, 255));
		textRole.setBounds(195, 147, 148, 40);
		panel_1.add(textRole);
		
		textAcces = new JTextField();
		textAcces.setEditable(false);
		textAcces.setColumns(10);
		textAcces.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "N.Acceso", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textAcces.setBackground(new Color(128, 128, 255));
		textAcces.setBounds(353, 147, 148, 40);
		panel_1.add(textAcces);
		
		textHour = new JTextField();
		textHour.setEditable(false);
		textHour.setColumns(10);
		textHour.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Hora Entrada/Salida", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		textHour.setBackground(new Color(128, 128, 255));
		textHour.setBounds(511, 147, 214, 40);
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
		
		btnBackLogin = new JButton("Relogear");
		if (nivel.equals("Gandalf")) {
			btnBackLogin.setVisible(false);
		} else {
			btnBackLogin.setVisible(true);
		}
		btnBackLogin.addActionListener(this);
		btnBackLogin.setBounds(296, 126, 134, 23);
		panel_2.add(btnBackLogin);
		
	}

	/**
	 * Maneja acciones de botones del formulario (registro temporal, volver al login).
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(btnTempUser == e.getSource()) {
			
			usuarioTemporal();
		}
		if (btnBackLogin == e.getSource()) {
			miConexion.desconectar();
			this.dispose();
			LoginForm login = new LoginForm();
			login.setVisible(true);
		}
		
	}

	/**
	 * Abre el diálogo de registro de usuario temporal y delega el callback de
	 * lectura serie al nuevo diálogo mientras esté abierto.
	 */
	private void usuarioTemporal() {
		
		final SerialDataCallback mainCallback = this;
		
		TemporaryUserRegister dialog = new TemporaryUserRegister();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		miConexion.setSerialDataCallback(dialog);
		
		dialog.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
	            
	            System.out.println("Diálogo cerrado. Devolviendo callback a MainWindow...");
	            miConexion.setSerialDataCallback(mainCallback);
	        }
	    });
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	/**
	 * Prepara los campos de la vista con la fila seleccionada en la tabla de
	 * historial. Inicia un temporizador para limpiar los datos después de 10s.
	 */
	private void prepararUsuario() {
		
		if (clearDataTimer != null && clearDataTimer.isRunning()) {
	        clearDataTimer.stop();
	    }
		
		int fila = table.getSelectedRow();
		
		if(fila != -1) {
			
			String nombre = modelo.getValueAt(fila, 0).toString();
			String apellido1 = modelo.getValueAt(fila, 1).toString();
			String entradaSalida = modelo.getValueAt(fila, 2).toString();
			
			ConexionDB conex = new ConexionDB();
			ResultadoIdentificacion result= conex.buscarEmpleadoPorNombredeTabla(nombre, apellido1);
			Object[] usuario = conex.obtenerDatosCompletos(result.idEntidad(), result.tipoEntidad());
			
			if (result.tipoEntidad().equals("empleado")) {
				
				textName.setText(String.valueOf(usuario[0]));
				textLastName1.setText(String.valueOf(usuario[1]));
				textLasName2.setText(String.valueOf(usuario[2]));
				textDNI.setText(String.valueOf(usuario[3]));
				textGender.setText(String.valueOf(usuario[4]));
				textRole.setText(String.valueOf(usuario[5]));
				textEmail.setText(String.valueOf(usuario[6]));
				textAcces.setText(String.valueOf(usuario[7]));
				if (usuario[8] != null && usuario[8] instanceof java.io.File) {
			        
					java.io.File fotoFile = (java.io.File) usuario[8];
		            // Creo el icono original
		            javax.swing.ImageIcon icono = new javax.swing.ImageIcon(fotoFile.getAbsolutePath());
		            // Llamo al nuevo método para que lo escale y lo muestre
		            mostrarFotoEscalada(icono); 
				    
				} else {
				   
				    lblPicture.setIcon(null); 
				}
				
				
			} else if (result.tipoEntidad().equals("temporal")) {
				
				textName.setText(String.valueOf(usuario[0]));
				textLastName1.setText(String.valueOf(usuario[1]));
				textLasName2.setText(String.valueOf(usuario[2]));
				textDNI.setText(String.valueOf(usuario[3]));
				textGender.setText("N/A");
				textRole.setText("Visita Temporal");
				textEmail.setText("N/A");
				textAcces.setText("N/A");
				if (usuario[5] != null && usuario[5] instanceof java.io.File) {
			        
					java.io.File fotoFile = (java.io.File) usuario[5];
		            // Creo el icono original
		            javax.swing.ImageIcon icono = new javax.swing.ImageIcon(fotoFile.getAbsolutePath());
		            // Llamo al nuevo método para que lo escale y lo muestre
		            mostrarFotoEscalada(icono);
				    
				} else {
				   
				    lblPicture.setIcon(null); 
				}
			}
			
			textHour.setText(entradaSalida);
			// Inicio un temporizador para limpiar los datos después de 10 segundos
			clearDataTimer = new javax.swing.Timer(10000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					limpiarCampos();
				}
			});
			clearDataTimer.setRepeats(false); // Solo se ejecuta una vez
			clearDataTimer.start();
		
		
		}

	}

	/**
	 * Callback llamado cuando llega una línea desde la comunicación serie.
	 *
	 * @param dato cadena recibida desde el dispositivo serie (tag RFID).
	 */
	@Override
	public void onDatoRecibido(String dato) {
		mensajeDeArduino = dato;
		
		entradaUsuario();
		
		
	}

	/**
	 * Añade una fila a la tabla de historial en memoria.
	 *
	 * @param nombre   nombre del usuario
	 * @param apellido primer apellido del usuario
	 * @param ts       marca temporal (string) de la entrada/salida
	 */
	private void actualizarTabla(String nombre, String apellido, String ts) {
		
		modelo.addRow(new Object[] {nombre, apellido, ts});
		table.setModel(modelo);
		
		
	}

	/**
	 * Llama a la lógica para registrar en base de datos la entrada/salida.
	 *
	 * @param idEntidad id de la entidad a registrar
	 */
	private void registrarEntradasYSalidas(int idEntidad) {
		ConexionDB conex = new ConexionDB();
		conex.registrarEntradaSalida(idEntidad);
	}

	/**
	 * Procesa el tag leído: busca la entidad, muestra datos, registra y
	 * confirma la lectura con el lector enviando una letra 'P'.
	 */
	private void entradaUsuario() {
		
		if (clearDataTimer != null && clearDataTimer.isRunning()) {
	        clearDataTimer.stop();
	    }
		
		try {
			
			ConexionDB conex = new ConexionDB();
			ResultadoIdentificacion resultado = conex.buscarEmpleadoPorTag(mensajeDeArduino);
			
		    // Si es nulo (tag no encontrado), limpiamos campos y salimos del método.
		    if (resultado == null) {
		        System.err.println("Tag no reconocido: " + mensajeDeArduino);
		        limpiarCampos();
		        return; // Salimos para no ejecutar el código de abajo
		    }
		    
	        if (resultado.tipoEntidad().equals("empleado")) {
				
				Object[] usuario = conex.obtenerDatosCompletos(resultado.idEntidad(), resultado.tipoEntidad());
				textName.setText(String.valueOf(usuario[0]));
				textLastName1.setText(String.valueOf(usuario[1]));
				textLasName2.setText(String.valueOf(usuario[2]));
				textDNI.setText(String.valueOf(usuario[3]));
				textGender.setText(String.valueOf(usuario[4]));
				textRole.setText(String.valueOf(usuario[5]));
				textEmail.setText(String.valueOf(usuario[6]));
				textAcces.setText(String.valueOf(usuario[7]));
				if (usuario[8] != null && usuario[8] instanceof java.io.File) {
			        
					java.io.File fotoFile = (java.io.File) usuario[8];
		            // Creo el icono original
		            javax.swing.ImageIcon icono = new javax.swing.ImageIcon(fotoFile.getAbsolutePath());
		            // Llamo al nuevo método para que lo escale y lo muestre
		            mostrarFotoEscalada(icono); 
				    
				} else {
				   
				    lblPicture.setIcon(null); 
				}
				
				
			} else if (resultado.tipoEntidad().equals("temporal")) {
				
				Object[] usuario = conex.obtenerDatosCompletos(resultado.idEntidad(), resultado.tipoEntidad());
				textName.setText(String.valueOf(usuario[0]));
				textLastName1.setText(String.valueOf(usuario[1]));
				textLasName2.setText(String.valueOf(usuario[2]));
				textDNI.setText(String.valueOf(usuario[3]));
				textGender.setText("N/A");
				textRole.setText("Visita Temporal");
				textEmail.setText("N/A");
				textAcces.setText("N/A");
				if (usuario[5] != null && usuario[5] instanceof java.io.File) {
			        
					java.io.File fotoFile = (java.io.File) usuario[5];
		            // Creo el icono original
		            javax.swing.ImageIcon icono = new javax.swing.ImageIcon(fotoFile.getAbsolutePath());
		            // Llamo al nuevo método para que lo escale y lo muestre
		            mostrarFotoEscalada(icono);
				    
				} else {
				   
				    lblPicture.setIcon(null); 
				}
			}
			
			DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); 
			String ts = LocalDateTime.now().format(f);
			textHour.setText(ts);
			
			actualizarTabla(textName.getText(), textLastName1.getText(), ts);
			
			registrarEntradasYSalidas(resultado.idEntidad());
		} catch (Exception e) {
	        // 1. Imprimo el error en la consola para saber qué pasó
	        System.err.println("Error al procesar el tag: " + e.getMessage());
	        e.printStackTrace();
	        
	        // 2. Limpio los campos para que el usuario vea que el tag falló
	        limpiarCampos();
	    }
		
		// Inicio un temporizador para limpiar los datos después de 10 segundos
		clearDataTimer = new javax.swing.Timer(10000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				limpiarCampos();
			}
		});
		clearDataTimer.setRepeats(false); // Solo se ejecuta una vez
		clearDataTimer.start();
		miConexion.enviarLetra('P'); // Enviar señal de confirmación a Arduino
	}

	/**
	 * Limpia todos los campos de la vista dejando la interfaz vacía.
	 */
	private void limpiarCampos() {
		textName.setText("");
		textLastName1.setText("");
		textLasName2.setText("");
		textDNI.setText("");
		textGender.setText("");
		textRole.setText("");
		textEmail.setText("");
		textAcces.setText("");
		textHour.setText("");
		lblPicture.setIcon(null);
		
	}

	/**
	 * Muestra una imagen escalada en el JLabel de foto manteniendo la proporción.
	 *
	 * @param iconoOriginal icono original a escalar
	 */
	private void mostrarFotoEscalada(ImageIcon iconoOriginal) {
		
		// Escalo la imagen al tamaño del JLabel
	    int anchoLabel = lblPicture.getWidth();
	    int altoLabel = lblPicture.getHeight();
	    java.awt.Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(anchoLabel, altoLabel, java.awt.Image.SCALE_SMOOTH);
	    ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
	    
	    // Establezco el icono escalado en el JLabel
	    lblPicture.setIcon(iconoEscalado);
		
	}
}
