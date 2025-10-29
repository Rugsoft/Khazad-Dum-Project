package khazaddum.operaciones;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Maneja la comunicación serie con un dispositivo (como Arduino)
 * usando jSerialComm y un listener para recibir datos asíncronamente.
 */
public class ComunicacionSerie implements SerialPortDataListener {

    private SerialPort puertoSerie;
    private BufferedReader serialReader; // Sigue siendo necesario para leer líneas completas
    private SerialDataCallback miCallback;

    /**
     * Conecta al puerto serie e inicializa el listener.
     */
    public boolean conectar() {
        puertoSerie = SerialPort.getCommPort("COM4"); // Cambia esto por tu puerto
        puertoSerie.setBaudRate(9600); // O la velocidad que uses

        if (!puertoSerie.openPort()) {
            System.err.println("Error: No se pudo abrir el puerto.");
            return false;
        }

        // 1. Inicializo el BufferedReader
        serialReader = new BufferedReader(new InputStreamReader(puertoSerie.getInputStream()));
        
        // 2. Registro ESTA CLASE (this) como el listener
        puertoSerie.addDataListener(this);
        System.out.println("Puerto conectado y listener registrado.");
        return true;
    }

    /**
     * Método de envío de un carácter al dispositivo serie.
     */
    public boolean enviarLetra(char valor) {
        try {
            puertoSerie.getOutputStream().write(valor);
            puertoSerie.getOutputStream().flush();
            return true;
        } catch (IOException ex) {
            System.err.println("Error al enviar datos: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Cierra el puerto y libera recursos.
     */
    public void desconectar() {
        if (puertoSerie != null && puertoSerie.isOpen()) {
            puertoSerie.removeDataListener(); // Importante: quitar el listener
            puertoSerie.closePort();
            System.out.println("Puerto desconectado.");
        }
    }

    // --- MÉTODOS REQUERIDOS POR SerialPortDataListener ---

    /**
     * (Método 1 de 2)
     * Le dice a la librería qué eventos nos interesan.
     * Solo queremos que nos avise cuando haya datos para leer.
     */
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    /**
     * (Método 2 de 2)
     * Este método es llamado AUTOMÁTICAMENTE por jSerialComm
     * en un hilo separado cuando llegan datos.
     */
    @Override
    public void serialEvent(SerialPortEvent event) {
        // Comprobamos si el evento es de "datos disponibles"
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            return; // Si no, ignoramos el evento
        }

        try {
            // Usamos serialReader.ready() para asegurarnos de que hay
            // una línea completa lista para ser leída.
            // Leemos todas las líneas que hayan llegado.
            while (serialReader.ready()) {
                String lineaRecibida = serialReader.readLine();
                
             // Si alguien se registró (miCallback no es null),
                // llámalo y entrégale los datos.
                if (this.miCallback != null) {
                    this.miCallback.onDatoRecibido(lineaRecibida);
                }
                // ¡Aquí procesas la línea recibida!
                System.out.println("Arduino dice: " + lineaRecibida);


            }
        } catch (IOException ex) {
            System.err.println("Error al leer del puerto: " + ex.getMessage());
        }
    }
    
    public interface SerialDataCallback {
        /**
         * @param dato El String de datos recibido desde el puerto serie.
         */
        void onDatoRecibido(String dato);
    }
    
    /**
     * Tu programa principal llamará a esto para "registrar"
     * su método de callback.
     * @param callback La instancia que implementa SerialDataCallback
     */
    public void setSerialDataCallback(SerialDataCallback callback) {
        this.miCallback = callback;
    }
}
