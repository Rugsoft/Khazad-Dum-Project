package khazzadum.main;

import java.awt.EventQueue;

import javax.swing.JDialog;

import khazaddum.gui.LoginForm;

/**
 * Punto de entrada de la aplicación.
 * <p>
 * Inicializa la interfaz gráfica en el hilo de despacho de AWT/Swing.
 * </p>
 */
public class Main {

    /**
     * Método main que arranca la aplicación.
     *
     * @param args argumentos de línea de comandos (no usados).
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginForm dialog = new LoginForm();
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}