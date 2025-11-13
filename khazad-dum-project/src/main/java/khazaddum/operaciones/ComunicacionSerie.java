package khazaddum.operaciones;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import khazaddum.gui.MainWindowBalrog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maneja la comunicación serie con un dispositivo (por ejemplo un Arduino).
 * <p>
 * Esta clase implementa {@code SerialPortDataListener} y expone métodos para
 * conectar/desconectar, enviar datos y recibir notificaciones asíncronas cuando
 * llegan datos por el puerto serie. Además permite registrar un callback
 * {@link SerialDataCallback} para recibir las líneas completas leídas.
 * </p>
 */
public class ComunicacionSerie implements SerialPortDataListener {

    private SerialPort puertoSerie;
    private static final Logger logger = LoggerFactory.getLogger(ComunicacionSerie.class);
    private BufferedReader serialReader; // Sigue siendo necesario para leer líneas completas
    private SerialDataCallback miCallback;

    /**
     * Conecta al puerto serie e inicializa el listener.
     *
     * @return {@code true} si la conexión se abrió correctamente, {@code false}
     *         en caso contrario.
     */
    public boolean conectar() {
        puertoSerie = SerialPort.getCommPort("COM4"); // Puerto serie a usar
        puertoSerie.setBaudRate(9600); // Velocidad en baudios

        if (!puertoSerie.openPort()) {
        	logger.error("No se pudo abrir el puerto serie COM4.");
            System.err.println("Error: No se pudo abrir el puerto.");
            return false;
        }

        // 1. Inicializo el BufferedReader
        serialReader = new BufferedReader(new InputStreamReader(puertoSerie.getInputStream()));
        
        // 2. Registro ESTA CLASE (this) como el listener
        puertoSerie.addDataListener(this);
        logger.info("Puerto serie COM4 abierto y listener registrado.");
        System.out.println("Puerto conectado y listener registrado.");
        return true;
    }

    /**
     * Envía un único carácter por el puerto serie.
     *
     * @param valor carácter a enviar.
     * @return {@code true} si el envío tuvo éxito, {@code false} si ocurrió un
     *         error de E/S.
     */
    public boolean enviarLetra(char valor) {
        try {
            puertoSerie.getOutputStream().write(valor);
            puertoSerie.getOutputStream().flush();
            return true;
        } catch (IOException ex) {
        	logger.error("Error al enviar datos por el puerto serie: {}", ex.getMessage());
            System.err.println("Error al enviar datos: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Cierra el puerto serie y libera recursos asociados.
     */
    public void desconectar() {
        if (puertoSerie != null && puertoSerie.isOpen()) {
            puertoSerie.removeDataListener(); // Importante: quitar el listener
            puertoSerie.closePort();
            System.out.println("Puerto desconectado.");
            logger.info("Puerto serie COM4 desconectado.");
        }
    }

    // --- MÉTODOS REQUERIDOS POR SerialPortDataListener ---

    /**
     * Indica a la librería qué eventos nos interesa escuchar (datos disponibles).
     *
     * @return uno o más flags de escucha (por ejemplo
     *         {@link SerialPort#LISTENING_EVENT_DATA_AVAILABLE}).
     */
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    /**
     * Método llamado por jSerialComm en un hilo de trabajo cuando llegan datos.
     * Este método lee líneas completas del stream y notifica al callback
     * registrado mediante {@link #setSerialDataCallback(SerialDataCallback)}.
     *
     * @param event evento recibido de jSerialComm.
     */
    @Override
    public void serialEvent(SerialPortEvent event) {
        // Comprobamos si el evento es de "datos disponibles"
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            return; // Si no, ignoramos el evento
        }

        try {
            // Uso serialReader.ready() para asegurarme de que hay
            // una línea completa lista para ser leída.
            // Leo todas las líneas que hayan llegado.
            while (serialReader.ready()) {
                String lineaRecibida = serialReader.readLine();
                
             // Si alguien se registró (miCallback no es null),
                // lo llamo y le entrego los datos.
                if (this.miCallback != null) {
                    this.miCallback.onDatoRecibido(lineaRecibida);
                }
                // ¡Aquí proceso la línea recibida!
                System.out.println("Arduino dice: " + lineaRecibida);


            }
        } catch (IOException ex) {
        	logger.error("Error al leer del puerto serie: {}", ex.getMessage());
            System.err.println("Error al leer del puerto: " + ex.getMessage());
        }
    }
    
    /**
     * Interfaz de callback que debe implementar el consumidor para recibir
     * líneas completas leídas desde el puerto serie.
     */
    public interface SerialDataCallback {
        /**
         * Invocado cuando llega una línea completa desde el puerto serie.
         *
         * @param dato String con la línea recibida (sin el separador de línea).
         */
        void onDatoRecibido(String dato);
    }
    
    /**
     * Registra el callback que recibirá las líneas leídas por el puerto serie.
     *
     * @param callback instancia que implementa {@link SerialDataCallback}.
     */
    public void setSerialDataCallback(SerialDataCallback callback) {
        this.miCallback = callback;
    }
}