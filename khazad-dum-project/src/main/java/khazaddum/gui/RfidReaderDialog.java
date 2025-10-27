package khazaddum.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

// Importamos la biblioteca jSerialComm
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

public class RfidReaderDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private String codigoTagLeido;

	// Objeto para manejar el puerto serie
	private SerialPort comPort;
	private JLabel lblInstruccion;

	public RfidReaderDialog(JDialog parent) {
		super(parent, "Leer Tag RFID", true); // Modal
		this.codigoTagLeido = null;

		setBounds(100, 100, 450, 200);
		setLocationRelativeTo(parent);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		lblInstruccion = new JLabel("Buscando Arduino...");
		lblInstruccion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInstruccion.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblInstruccion, BorderLayout.CENTER);

		// Manejar el clic en la 'X' para cerrar el puerto correctamente
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				detenerConexionArduino();
			}
		});

		// Iniciar la conexión al construir la ventana
		// Lo hacemos en un hilo separado para no congelar la GUI
		new Thread(this::iniciarConexionArduino).start();
	}

	private void iniciarConexionArduino() {
	    comPort = encontrarPuertoArduino();
	    if (comPort == null) {
	        JOptionPane.showMessageDialog(this, "No se pudo encontrar el Arduino. Asegúrese de que esté conectado.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
	        SwingUtilities.invokeLater(this::dispose);
	        return;
	    }

	    comPort.setBaudRate(9600);
	    comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

	    // === DESACTIVAR DTR ===
	    comPort.setDTR();

	    if (!comPort.openPort()) {
	        JOptionPane.showMessageDialog(this, "No se pudo abrir el puerto serie.", "Error", JOptionPane.ERROR_MESSAGE);
	        SwingUtilities.invokeLater(this::dispose);
	        return;
	    }

	    // === ESPERAR 2 SEGUNDOS PARA QUE ARDUINO SE REINICIE (si ocurre) ===
	    try {
	        Thread.sleep(2000);
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt();
	    }

	    // === LIMPIAR BUFFER POR SI HAY DATOS RESIDUALES ===
	    comPort.clearDTR();
	    comPort.clearRTS();

	    // === ESPERAR "ARDUINO_READY" CON TIMEOUT ===
	    boolean arduinoListo = false;
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(comPort.getInputStream()))) {
	        String linea;
	        long startTime = System.currentTimeMillis();
	        
	        while (System.currentTimeMillis() - startTime < 10000) { // Timeout 10 segundos
	            if (reader.ready()) {
	                linea = reader.readLine();
	                if (linea != null) {
	                    linea = linea.trim();
	                    System.out.println("Handshake recibido: " + linea);
	                    
	                    if ("ARDUINO_READY".equals(linea)) {
	                        System.out.println("Arduino está listo para recibir comandos.");
	                        arduinoListo = true;
	                        break;
	                    }
	                }
	            } else {
	                Thread.sleep(100); // Pequeña pausa para no saturar la CPU
	            }
	        }
	        
	        if (!arduinoListo) {
	            throw new IOException("Timeout: Arduino no respondió con ARDUINO_READY");
	        }
	        
	    } catch (IOException | InterruptedException e) {
	        JOptionPane.showMessageDialog(this, "Error al comunicarse con Arduino: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        SwingUtilities.invokeLater(() -> dispose());
	        return;
	    }

	    // === ENVIAR 'R' ===
	    byte[] comando = {'R'};
	    comPort.writeBytes(comando, 1);
	    try {
	        comPort.getOutputStream().flush();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    // === INICIAR ESCUCHA DEL RFID ===
	    iniciarEscuchaRFID();
	}
	
	private SerialPort encontrarPuertoArduino() {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Buscando puertos..."); // Ayuda para depurar
        
        for (SerialPort port : ports) {
            String descripcion = port.getPortDescription().toLowerCase();
            System.out.println("Puerto encontrado: " + port.getSystemPortName() + " (" + descripcion + ")"); // Ayuda para depurar

            // Modificación: Añadimos "ch340" a la comprobación
            if (descripcion.contains("arduino") || 
                descripcion.contains("usb serial") || 
                descripcion.contains("ch340")) {
                
                System.out.println("¡Puerto Arduino/clon encontrado!");
                return port;
            }
        }
        
        System.out.println("No se encontró ningún puerto compatible.");
        return null; // No se encontró
    }

	/**
	 * Inicia un listener en un hilo separado que espera la respuesta
	 * (el UID) del Arduino.
	 */
	private void iniciarEscuchaRFID() {
		System.out.println("Inicio Escucha (esperando 'READY'...)");
		new Thread(() -> {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(comPort.getInputStream()))) {
				
				String tagLeido;
				
				// Usamos un bucle while para leer líneas
				while ((tagLeido = reader.readLine()) != null) {
					
					tagLeido = tagLeido.trim();
					System.out.println("Línea recibida: " + tagLeido);

					// 1. ¿Es el saludo del Arduino?
					if (tagLeido.equals("READY")) {
						System.out.println("¡Arduino está LISTO!");
						// Actualizar la GUI
						SwingUtilities.invokeLater(() -> {
							lblInstruccion.setText("Por favor, acerque el tag RFID al lector...");
						});
					
					// 2. ¿Es un tag? (Asumimos que un tag tiene más de 4 caracteres)
					} else if (tagLeido.length() > 4) {
						System.out.println("¡Tag RECIBIDO!");
						final String tagFinal = tagLeido;
						
						// Enviar a procesar y cerrar
						SwingUtilities.invokeLater(() -> {
							procesarTagLeido(tagFinal);
						});
						
						break; // Salir del bucle while, ya tenemos el tag
					}
				}
				
			} catch (IOException e) {
				if (!e.getMessage().contains("Port closed")) {
					e.printStackTrace();
				}
			}
			System.out.println("Hilo de escucha terminado.");
		}).start();
	}

	/**
	 * Este método se llama (en el hilo de Swing) cuando se recibe el tag.
	 */
	private void procesarTagLeido(String tag) {
		this.codigoTagLeido = tag; // Guardamos el tag
		detenerConexionArduino(); // Limpiamos la conexión
		this.dispose(); // Cerramos esta ventana modal
	}

	/**
	 * Cierra el puerto serie de forma segura.
	 */
	private void detenerConexionArduino() {
		if (comPort != null && comPort.isOpen()) {
			comPort.removeDataListener();
			comPort.closePort();
		}
	}

	/**
	 * Método para que la ventana anterior recoja el resultado.
	 */
	public String getCodigoTagLeido() {
		return codigoTagLeido;
	}
}