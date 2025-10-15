package khazaddum.operaciones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
}
