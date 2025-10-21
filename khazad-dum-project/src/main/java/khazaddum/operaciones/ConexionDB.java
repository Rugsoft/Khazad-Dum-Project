package khazaddum.operaciones;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.swing.JOptionPane;

import khazaddum.modelo.Empleado;
import khazaddum.modelo.VisitaTemporal;

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
	
	public Empleado buscarEmpleadoPorNombredeTabla(String nombre, String apellido) {
		
		String sql = "SELECT * FROM empleados WHERE nombre = ? AND apellido1 = ?";
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setString(1, nombre);
				sentencia.setString(2, apellido);
				
				ResultSet resultado = sentencia.executeQuery();
				boolean encontrado = true;
				
				while (resultado.next()) {
					
					if (encontrado) {
						
						int idEntidad = resultado.getInt("id_entidad");
						String nombreEmp = resultado.getString("nombre");
						String apellido1 = resultado.getString("apellido1");
						String apellido2 = resultado.getString("apellido2");
						String dni = resultado.getString("dni");
						String genero = resultado.getString("genero");
						String puesto = resultado.getString("puesto");
						String email = resultado.getString("email");
						int nivelAcceso = Integer.parseInt(resultado.getString("nivel_acceso"));
						byte[] foto = resultado.getBytes("foto");
						Empleado empleado = new Empleado(nombreEmp, apellido1, apellido2, dni, genero, puesto, email, nivelAcceso, foto);
						encontrado = false;
						return empleado;
					
					}
					
				}
				
			}
		} catch(SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("No se ha podido comprobar el nombre: " + e.getMessage());
		}
		return null;
	}
	
	public static void añadirUsuarioTemporal(Object[] datos) throws FileNotFoundException {
		
		String sqlEntidad = "INSERT INTO entidades (tipo_entidad) VALUES ('temporal')";
		String sqlTemporal = "INSERT INTO usuarios_temporales (id_entidad, nombre, apellido1, apellido2, dni, motivo_visita, foto, fecha_expiracion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		int idEntidad = -1;
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				
				PreparedStatement psEntidad = conexion.prepareStatement(sqlEntidad, Statement.RETURN_GENERATED_KEYS);
				psEntidad.executeUpdate();
				ResultSet rsKeys = psEntidad.getGeneratedKeys();
				
				if (rsKeys.next()) {
					idEntidad = rsKeys.getInt(1);
					
				} else {
	                throw new SQLException("No se pudo obtener el ID de la entidad.");
	            }
				
				PreparedStatement psTemporal = conexion.prepareStatement(sqlTemporal);
				psTemporal.setInt(1, idEntidad);
				
				for(int i = 0; i < datos.length; i++) {
					if (i == 5) {
						File picture = (File) datos[i];
						InputStream inputStream = new FileInputStream(picture);
						psTemporal.setBinaryStream(7, inputStream, (int) picture.length());
					} else if (i == 6) {
						int horas = (int) datos[i];
						LocalDateTime fechaExpiracion = LocalDateTime.now().plusHours(horas);
			            Timestamp sqlFechaExpiracion = Timestamp.valueOf(fechaExpiracion);
			            psTemporal.setTimestamp(8, sqlFechaExpiracion);
					} else {
						psTemporal.setObject(i + 2, datos[i]);
					}
					
				}
				psTemporal.executeUpdate();
				JOptionPane.showMessageDialog(null, "Usuario temporal añadido", "INFO", JOptionPane.INFORMATION_MESSAGE);
			
			}
					
				} catch(SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
					System.out.println("No se ha podido comprobar el nombre: " + e.getMessage());
				}
				
		}
}