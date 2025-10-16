package khazaddum.operaciones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	
	public static boolean añadirUsuariosLogin(String sql, Object[] datos) {
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				
				for(int i = 0; i < datos.length; i++) {
					sentencia.setObject(i + 1, datos[i]);
				}
				
				sentencia.executeUpdate();
				System.out.println("Usuario registrado");
				JOptionPane.showMessageDialog(null, "Usuario registrado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
				return true;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("No se ha podido registrar el usuario, error: " + e.getMessage());
		}
		return false;

	}
	
	public static String comprobarLogin(String sql, String user, String password) {
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setString(1, user);
				sentencia.setString(2, password);
				
				var resultado = sentencia.executeQuery();
				
				if (resultado.next()) {
					String nivel = resultado.getString("nivel");
					return nivel;
				} else {
					System.out.println("Login incorrecto");
					return null;
				}
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("No se ha podido comprobar el login, error: " + e.getMessage());
			return null;
		}
		return null;
	}
	
	public void buscarEmpleadoPorNombredeTabla(String nombre, String apellido) {
		
		String sql = "SELECT e.id_empleado, e.nombre, e.apellido1, e.apellido2, e.dni, e.genero, e.puesto, e.email, e.nivel_acceso, r.fecha_hora, r.tipo_registro " +
                "FROM empleados e " +
                "INNER JOIN registros_fichajes r ON e.id_empleado = r.id_empleado " +
                "WHERE e.nombre = ? AND e.apellido = ? " +
                "ORDER BY r.fecha_hora DESC";
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setString(1, nombre);
				sentencia.setString(2, apellido);
				
				ResultSet resultado = sentencia.executeQuery();
				boolean encontrado = true;
				
				while (resultado.next()) {
					
					if (encontrado) {
						
						int idEmpleado = resultado.getInt("id_empleado");
						String nombreEmp = resultado.getString("nombre");
						String apellido1 = resultado.getString("apellido1");
						String apellido2 = resultado.getString("apellido2");
						String dni = resultado.getString("dni");
						String genero = resultado.getString("genero");
						String puesto = resultado.getString("puesto");
						String email = resultado.getString("email");
						String nivelAcceso = resultado.getString("nivel_acceso");
						encontrado = false;
					
					}
					
					String fechaHora = resultado.getString("fecha_hora");
					String tipoRegistro = resultado.getString("tipo_registro");
					
					
				}
				
			}
		} catch(SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("No se ha podido comprobar el nombre: " + e.getMessage());
		}
	}
}