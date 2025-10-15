package khazaddum.operaciones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConexionDB {

	private static final String Controlador = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/khazad-dum-db";
	private static final String Usuario = "root";
	private static final String Contraseña = "";
	
	static {
		
		try {
			
			Class.forName(Controlador);
			System.out.println("Controlador cargado");
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Controlador no cargado");
		}
	}
	
	public static Connection conectar() {
		
		Connection conexion = null;
		
		try {
			
			conexion = DriverManager.getConnection(URL, Usuario, Contraseña);
			System.out.println("Conexion realizada");
			
		} catch(SQLException e){
			
			e.printStackTrace();
			System.out.println("Conexion no realizada, error: " + e.getMessage());
		}
		
		return conexion;
	}
	
	public static void añadirUsuariosLogin(String sql, Object[] datos) {
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				
				for(int i = 0; i < datos.length; i++) {
					sentencia.setObject(i + 1, datos[i]);
				}
				
				sentencia.executeUpdate();
				System.out.println("Usuario registrado");
				JOptionPane.showMessageDialog(null, "Usuario registrado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("No se ha podido registrar el usuario, error: " + e.getMessage());
		}
		
	}
}
