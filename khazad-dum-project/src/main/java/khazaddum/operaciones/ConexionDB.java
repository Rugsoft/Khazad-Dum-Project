package khazaddum.operaciones;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import khazaddum.modelo.Empleado;
import khazaddum.modelo.ResultadoIdentificacion;
import khazaddum.modelo.VisitaTemporal;
import khazaddum.modelo.RegistroUsuarios;

/**
 * Utilidad para la conexión y operaciones con la base de datos MySQL.
 * <p>
 * Proporciona métodos estáticos y de instancia para conectar a la base,
 * ejecutar inserciones, actualizaciones, búsquedas y operaciones relacionadas
 * con entidades como empleados y visitantes temporales.
 * </p>
 */
public class ConexionDB {

	private static final String Controlador = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/khazad-dum-db";
	private static final String Usuario = "root";
	private static final String Contraseña = "";
	ArrayList<Empleado> empList;
	ArrayList<VisitaTemporal> tempList;
	
	static {
		
		try {
			
			Class.forName(Controlador);
			System.out.println("Controlador cargado");
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Controlador no cargado");
		}
	}
	
	/**
	 * Crea y devuelve una conexión a la base de datos usando los parámetros
	 * configurados en esta clase.
	 *
	 * @return una instancia de {@link Connection} o {@code null} si no se
	 *         pudo establecer la conexión.
	 */
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
	
	/**
	 * Añade un usuario/login a la base de datos usando una consulta preparada y
	 * los datos proporcionados.
	 *
	 * @param sql   sentencia SQL INSERT con marcadores de posición
	 * @param datos valores que se asignarán a la sentencia preparada
	 * @return {@code true} si la inserción tuvo éxito, {@code false} en caso
	 *         contrario.
	 */
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
	
	/**
	 * Comprueba las credenciales de inicio de sesión contra la tabla
	 * {@code login_usuarios} y devuelve el nivel si coinciden.
	 *
	 * @param sql      sentencia SQL SELECT que devuelve la columna "nivel"
	 * @param user     nombre de usuario
	 * @param password contraseña
	 * @return el nivel del usuario (p. ej. "Gandalf") o {@code null} si no
	 *         se encuentra o hay un error.
	 */
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
	
	/**
	 * Busca un empleado (o usuario temporal) por nombre y primer apellido.
	 *
	 * @param nombre   nombre a buscar
	 * @param apellido primer apellido a buscar
	 * @return {@link ResultadoIdentificacion} con id y tipo, o {@code null} si
	 *         no se encuentra.
	 */
	public ResultadoIdentificacion buscarEmpleadoPorNombredeTabla(String nombre, String apellido) {
		
		String sql = "(SELECT E.id_entidad, E.tipo_entidad, EM.nombre, EM.apellido1, EM.apellido2 " +
                " FROM empleados EM JOIN entidades E ON EM.id_entidad = E.id_entidad " +
                " WHERE EM.nombre = ? AND EM.apellido1 = ?) " +
                "UNION ALL " +
                "(SELECT E.id_entidad, E.tipo_entidad, UT.nombre, UT.apellido1, UT.apellido2 " +
                " FROM usuarios_temporales UT JOIN entidades E ON UT.id_entidad = E.id_entidad " +
                " WHERE UT.nombre = ? AND UT.apellido1 = ?)";
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setString(1, nombre);
				sentencia.setString(2, apellido);
				sentencia.setString(3, nombre);
				sentencia.setString(4, apellido);
				
				ResultSet resultado = sentencia.executeQuery();
				
				if (resultado.next()) {
					int idEntidad = resultado.getInt("id_entidad");
                    String tipoEntidad = resultado.getString("tipo_entidad");
                    return new ResultadoIdentificacion(idEntidad, tipoEntidad);
                    
				} else {
					System.out.println("Tag no encontrado");
					return null;
				}
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("No se ha podido comprobar el tag: " + e.getMessage());
			return null;
		}
		return null;
		
	}
	
	/**
	 * Añade un nuevo empleado (inserta en entidades y empleados).
	 *
	 * @param datos array con los datos del empleado (incluida la foto como File)
	 * @throws FileNotFoundException si no se encuentra la foto indicada.
	 */
	public static void añadirEmpleado(Object[] datos) throws FileNotFoundException {
		
		String sqlEntidad = "INSERT INTO entidades (tipo_entidad) VALUES ('empleado')";
		String sqlEmpleado = "INSERT INTO empleados (id_entidad, nombre, apellido1, apellido2, dni, genero, puesto, email, nivel_acceso, foto, codigo_tag) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
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
				
				PreparedStatement psEmpleado = conexion.prepareStatement(sqlEmpleado);
				psEmpleado.setInt(1, idEntidad);
				
				for(int i = 1; i < datos.length; i++) {
					if (i == 8) {
						File picture = (File) datos[i];
						InputStream inputStream = new FileInputStream(picture);
						psEmpleado.setBinaryStream(10, inputStream, (int) picture.length());
					} else {
						psEmpleado.setObject(i + 1, datos[i]);
					}
					
				}
				psEmpleado.executeUpdate();
				JOptionPane.showMessageDialog(null, "Empleado añadido", "INFO", JOptionPane.INFORMATION_MESSAGE);
			
			}
					
				} catch(SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
				System.out.println("No se ha podido comprobar el nombre: " + e.getMessage());
			}
			
	}
	
	/**
	 * Añade un usuario temporal (inserta en entidades y usuarios_temporales).
	 *
	 * @param datos array con los datos del usuario temporal (incluida la foto y horas)
	 * @throws FileNotFoundException si no se encuentra la foto indicada.
	 */
	public static void añadirUsuarioTemporal(Object[] datos) throws FileNotFoundException {
		
		String sqlEntidad = "INSERT INTO entidades (tipo_entidad) VALUES ('temporal')";
		String sqlTemporal = "INSERT INTO usuarios_temporales (id_entidad, nombre, apellido1, apellido2, dni, motivo_visita, foto, fecha_expiracion, codigo_tag) " +
	                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
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
				
				for(int i = 1; i < datos.length - 1 ; i++) {
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
	
	/**
	 * Busca una entidad por su código de tag RFID y comprueba también si la
	 * visita temporal no ha expirado.
	 *
	 * @param tag código RFID a buscar
	 * @return {@link ResultadoIdentificacion} con id y tipo, o {@code null}
	 *         si no se encuentra.
	 */
	public ResultadoIdentificacion buscarEmpleadoPorTag(String tag) {
		
		String sql = "(SELECT E.id_entidad, E.tipo_entidad, EM.nombre, EM.apellido1 " +
                " FROM empleados EM JOIN entidades E ON EM.id_entidad = E.id_entidad " +
                " WHERE EM.codigo_tag = ?) " +
                "UNION ALL " +
                "(SELECT E.id_entidad, E.tipo_entidad, UT.nombre, UT.apellido1 " +
                " FROM usuarios_temporales UT JOIN entidades E ON UT.id_entidad = E.id_entidad " +
                " WHERE UT.codigo_tag = ? " + 
                " AND UT.fecha_expiracion > NOW())"; 
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setString(1, tag);
				sentencia.setString(2, tag);
				
				ResultSet resultado = sentencia.executeQuery();
				
				if (resultado.next()) {
					int idEntidad = resultado.getInt("id_entidad");
                    String tipoEntidad = resultado.getString("tipo_entidad");
                    return new ResultadoIdentificacion(idEntidad, tipoEntidad);
                    
				} else {
					System.out.println("Tag no encontrado");
					return null;
				}
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("No se ha podido comprobar el tag: " + e.getMessage());
			return null;
		}
		return null;
		
		
	}
	
	/**
	 * Obtiene todos los datos completos de una entidad (empleado o temporal)
	 * para mostrarlos en la interfaz.
	 *
	 * @param idEntidad   identificador de la entidad
	 * @param tipoEntidad "empleado" o "temporal"
	 * @return un array de Object con los datos formateados o {@code null} en
	 *         caso de error.
	 */
	public Object[] obtenerDatosCompletos(int idEntidad, String tipoEntidad) {
	    String sqlEmpleado = "SELECT * FROM empleados WHERE id_entidad = ?";
	    String sqlTemporal = "SELECT * FROM usuarios_temporales WHERE id_entidad = ?";

	    try (Connection conexion = conectar()) {

	        if (conexion != null) {
	            PreparedStatement sentencia;
	            if (tipoEntidad.equals("empleado")) {
	                sentencia = conexion.prepareStatement(sqlEmpleado);
	            } else if (tipoEntidad.equals("temporal")) {
	                sentencia = conexion.prepareStatement(sqlTemporal);
	            } else {
	                System.out.println("Tipo de entidad desconocido");
	                return null;
	            }

	            sentencia.setInt(1, idEntidad);
	            ResultSet resultado = sentencia.executeQuery();

	            if (resultado.next()) {
	                if (tipoEntidad.equals("empleado")) {
	                    // Procesar datos del empleado
	                	int idEmp = resultado.getInt("id_entidad");
						String nombreEmp = resultado.getString("nombre");
						String apellido1 = resultado.getString("apellido1");
						String apellido2 = resultado.getString("apellido2");
						String dni = resultado.getString("dni");
						String genero = resultado.getString("genero");
						String puesto = resultado.getString("puesto");
						String email = resultado.getString("email");
						int nivelAcceso = Integer.parseInt(resultado.getString("nivel_acceso"));
						byte[] foto = resultado.getBytes("foto");
						File fotoTemporal = null; // Inicializa como null
					    
					    if (foto != null && foto.length > 0) {
					        try {
					            // Creo un archivo temporal para la foto
					            fotoTemporal = File.createTempFile("user_" + dni + "_", ".jpg");
					            
					            // Escribo los bytes de la foto en el archivo temporal
					            Files.write(fotoTemporal.toPath(), foto);
					            
					            // Borro el archivo cuando la app se cierre
					            fotoTemporal.deleteOnExit(); 
					            
					        } catch (IOException e) {
					            System.err.println("Error al crear el archivo temporal de la foto: " + e.getMessage());
					        }
					    }
						String tag = resultado.getString("codigo_tag");
						Empleado empleado = new Empleado(idEmp, nombreEmp, apellido1, apellido2, dni, genero, puesto, email, nivelAcceso, fotoTemporal, tag);
						return empleado.crear();
						
		                } else if (tipoEntidad.equals("temporal")) {
	                    // Procesar datos del usuario temporal
	                    String nombre = resultado.getString("nombre");
	                    String apellido1 = resultado.getString("apellido1");
	                    String apellido2 = resultado.getString("apellido2");
	                    String dni = resultado.getString("dni");
	                    String motivoVisita = resultado.getString("motivo_visita");
	                    byte[] fotoBytes = resultado.getBytes("foto");
	                    File fotoTemporal = null; // Inicializa como null
	                    
	                    if (fotoBytes != null && fotoBytes.length > 0) {
	                        try {
	                            // Creo un archivo temporal para la foto
	                            fotoTemporal = File.createTempFile("user_" + dni + "_", ".jpg");
	                            
	                            // Escribo los bytes de la foto en el archivo temporal
	                            Files.write(fotoTemporal.toPath(), fotoBytes);
	                            
	                            // Borro el archivo cuando la app se cierre
	                            fotoTemporal.deleteOnExit(); 
	                            
	                        } catch (IOException e) {
	                            System.err.println("Error al crear el archivo temporal de la foto: " + e.getMessage());
	                        }
	                    }
	                    String codigoTag = resultado.getString("codigo_tag");
	                    VisitaTemporal temp = new VisitaTemporal(0, nombre, apellido1, apellido2, dni, motivoVisita, fotoTemporal, 0, codigoTag, null);
	                    return temp.crear();
	                }
	            } else {
	                System.out.println("No se encontraron datos para la entidad con ID: " + idEntidad);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
	        System.out.println("No se ha podido obtener los datos completos: " + e.getMessage());
	    }
		return null;
	}
	
	/**
	 * Registra una entrada o salida en la tabla registros para la entidad
	 * especificada.
	 *
	 * @param idEntidad identificador de la entidad.
	 */
	public void registrarEntradaSalida(int idEntidad) {
		
		String sql = "INSERT INTO registros (id_entidad, fecha_hora, tipo_registro) VALUES (?, ?, ?)";
		
		String tipoRegistro = tipoRegistro(idEntidad);
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setInt(1, idEntidad);
				sentencia.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
				sentencia.setString(3, tipoRegistro);
				
				sentencia.executeUpdate();
				System.out.println("Registro de entrada/salida añadido");
			
			}
					
				} catch(SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
				System.out.println("No se ha podido registrar la entrada/salida: " + e.getMessage());
			}
			
		
	}
	
	/**
	 * Determina el tipo de registro (entrada/salida) en función del último
	 * registro del día para la entidad indicada.
	 *
	 * @param idEntidad identificador de la entidad.
	 * @return "entrada" o "salida" según corresponda.
	 */
	public String tipoRegistro(int idEntidad) {
        
        String ultimoTipo = null; // Para guardar el último tipo de registro de hoy
        
        // Consulta para obtener el último registro de HOY
        String sql = "SELECT tipo_registro FROM registros " +
                     "WHERE id_entidad = ? AND DATE(fecha_hora) = CURDATE() " +
                     "ORDER BY fecha_hora DESC LIMIT 1";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idEntidad);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Si se encontró un registro de hoy, guardamos su tipo
                    ultimoTipo = rs.getString("tipo_registro");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar el último registro: " + e.getMessage());
            // En caso de error, es más seguro asumir 'entrada' para no bloquear
            return "entrada"; 
        }
        

        if (ultimoTipo == null) {
            // Caso 1: No hay registros hoy. El usuario está "fuera".
            // El próximo registro es ENTRADA.
            return "entrada";
            
        } else if (ultimoTipo.equals("entrada")) {
            // Caso 2: El último registro fue 'entrada'. El usuario está "dentro".
            // El próximo registro es SALIDA.
            return "salida";
            
        } else {
            // Caso 3: El último registro fue 'salida'. El usuario está "fuera".
            // El próximo registro es ENTRADA.
            return "entrada";
        }
    }
	
	/**
	 * Actualiza los datos de un empleado en la tabla empleados.
	 *
	 * @param idEmpleado identificador del empleado
	 * @param name nombre
	 * @param lastName1 primer apellido
	 * @param lastName2 segundo apellido
	 * @param dni DNI
	 * @param genero género
	 * @param puesto puesto
	 * @param email correo
	 * @param nivelAcceso nivel de acceso
	 */
	public void actualizarEmpleado(int idEmpleado,String name,String lastName1,String lastName2,String dni,String genero,String puesto,String email,int nivelAcceso) {
		
		String sql = "UPDATE empleados SET nombre = ?, apellido1 = ?, apellido2 = ?, dni = ?, genero = ?, puesto = ?, email = ?, nivel_acceso = ? WHERE id_entidad = ?";
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setString(1, name);
				sentencia.setString(2, lastName1);
				sentencia.setString(3, lastName2);
				sentencia.setString(4, dni);
				sentencia.setString(5, genero);
				sentencia.setString(6, puesto);
				sentencia.setString(7, email);
				sentencia.setInt(8, nivelAcceso);
				sentencia.setInt(9, idEmpleado);
				
				sentencia.executeUpdate();
				System.out.println("Empleado actualizado");
				JOptionPane.showMessageDialog(null, "Empleado actualizado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("No se ha podido actualizar el empleado, error: " + e.getMessage());
		}
	}
	
	/**
	 * Actualiza los datos de un usuario temporal.
	 *
	 * @param idTemporal id de la visita
	 * @param name nombre
	 * @param lastName1 primer apellido
	 * @param lastName2 segundo apellido
	 * @param dni dni
	 * @param motivoVisita motivo
	 */
	public void actualizarTemporal(int idTemporal,String name,String lastName1,String lastName2,String dni,String motivoVisita) {
		
		String sql = "UPDATE usuarios_temporales SET nombre = ?, apellido1 = ?, apellido2 = ?, dni = ?, motivo_visita = ? WHERE id_entidad = ?";
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setString(1, name);
				sentencia.setString(2, lastName1);
				sentencia.setString(3, lastName2);
				sentencia.setString(4, dni);
				sentencia.setString(5, motivoVisita);
				sentencia.setInt(6, idTemporal);
				
				sentencia.executeUpdate();
				System.out.println("Usuario temporal actualizado");
				JOptionPane.showMessageDialog(null, "Usuario temporal actualizado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("No se ha podido actualizar el usuario temporal, error: " + e.getMessage());
		}
	}
	
	/**
	 * Obtiene una lista de empleados a partir de la consulta SQL proporcionada.
	 *
	 * @param sql consulta SELECT que devuelve columnas de la tabla empleados
	 * @return lista de {@link Empleado}
	 */
	public ArrayList<Empleado> obtenerEmpleados(String sql) {
		
		
		ArrayList<Empleado> empList = new ArrayList<Empleado>();
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				
				ResultSet resultado = sentencia.executeQuery();
				
				while (resultado.next()) {
					
					int idEntidad = resultado.getInt("id_entidad");
					String nombre = resultado.getString("nombre");
					String apellido1 = resultado.getString("apellido1");
					String apellido2 = resultado.getString("apellido2");
					String dni = resultado.getString("dni");
					String genero = resultado.getString("genero");
					String puesto = resultado.getString("puesto");
					String email = resultado.getString("email");
					int nivelAcceso = resultado.getInt("nivel_acceso");
					
					Empleado emp = new Empleado(idEntidad, nombre, apellido1, apellido2, dni, genero, puesto, email, nivelAcceso, null, null);
					empList.add(emp);
				}
				
			}
					
				} catch(SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
				System.out.println("No se ha podido comprobar el nombre: " + e.getMessage());
			}
		
		return empList;
	}
	
	/**
	 * Obtiene la lista de usuarios temporales según la consulta SQL indicada.
	 *
	 * @param sql consulta SELECT
	 * @return lista de {@link VisitaTemporal}
	 */
	public ArrayList<VisitaTemporal> obtenerTemporal(String sql){
		
		ArrayList<VisitaTemporal> tempList = new ArrayList<VisitaTemporal>();
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				
				ResultSet resultado = sentencia.executeQuery();
				
				while (resultado.next()) {
					
					int idEntidad = resultado.getInt("id_entidad");
					String nombre = resultado.getString("nombre");
					String apellido1 = resultado.getString("apellido1");
					String apellido2 = resultado.getString("apellido2");
					String dni = resultado.getString("dni");
					String motivo = resultado.getString("motivo_visita");
					String fecha = resultado.getTimestamp("fecha_expiracion").toString();
					
					
					VisitaTemporal temp = new VisitaTemporal(idEntidad, nombre, apellido1, apellido2, dni, motivo, null, 0, null, fecha);
					tempList.add(temp);
				}
				
			}
					
				} catch(SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
				System.out.println("No se ha podido comprobar el nombre: " + e.getMessage());
			}
		
		return tempList;
		
	}
	
	/**
	 * Obtiene los registros de entradas/salidas de una entidad entre dos fechas
	 * y devuelve una lista de {@link RegistroUsuarios} con la información
	 * formateada.
	 *
	 * @param idEntidad identificador de la entidad
	 * @param fechaInicio fecha de inicio (inclusive)
	 * @param fechaFin fecha final (inclusive)
	 * @return lista de registros
	 */
	public ArrayList<RegistroUsuarios> obtenerRegistrosNombre(int idEntidad, LocalDate fechaInicio, LocalDate fechaFin){
		
		ArrayList<RegistroUsuarios> registros = new ArrayList<>();
		
		String sql = "SELECT R.fecha_hora, R.tipo_registro, " +
                "COALESCE(CONCAT(EM.nombre, ' ', EM.apellido1), CONCAT(UT.nombre, ' ', UT.apellido1)) AS nombre_completo " +
                "FROM registros AS R " +
                "JOIN entidades AS E ON R.id_entidad = E.id_entidad " +
                "LEFT JOIN empleados AS EM ON E.id_entidad = EM.id_entidad AND E.tipo_entidad = 'empleado' " +
                "LEFT JOIN usuarios_temporales AS UT ON E.id_entidad = UT.id_entidad AND E.tipo_entidad = 'temporal' " +
                "WHERE R.id_entidad = ? " +
                "  AND DATE(R.fecha_hora) >= ? " + 
                "  AND DATE(R.fecha_hora) <= ? " +
                "ORDER BY R.fecha_hora ASC";
		
		try (Connection conexion = conectar()){
			
			if(conexion != null) {
				PreparedStatement sentencia = conexion.prepareStatement(sql);
				sentencia.setInt(1, idEntidad);
				sentencia.setDate(2, java.sql.Date.valueOf(fechaInicio));
	            sentencia.setDate(3, java.sql.Date.valueOf(fechaFin));
				
				ResultSet resultado = sentencia.executeQuery();
				
				while (resultado.next()) {
					
					Timestamp fechaHora = resultado.getTimestamp("fecha_hora");
					String tipoRegistro = resultado.getString("tipo_registro");
					String nombreCompleto = resultado.getString("nombre_completo");
					
					LocalDateTime fecha = fechaHora.toLocalDateTime();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
					String fechaFormateada = fecha.format(formatter);
					
					RegistroUsuarios registro = new RegistroUsuarios(fechaFormateada, tipoRegistro, nombreCompleto);
					registros.add(registro);
				}
				
			}
					
				} catch(SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
				System.out.println("No se ha podido comprobar el nombre: " + e.getMessage());
			}
		
			return registros;
		
	}
	
	/**
	 * Elimina un usuario y su historial de registros en una transacción.
	 *
	 * @param id identificador de la entidad a eliminar
	 * @return {@code true} si la eliminación fue exitosa, {@code false}
	 *         en caso de error.
	 */
	public boolean eliminarUsuario(int id) {
		
		String sqlEntidad = "DELETE FROM entidades WHERE id_entidad = ?";
		String sqlRegistros = "DELETE FROM registros WHERE id_entidad = ?";
		
		Connection conexion = null;
		
		try {
			
			conexion = conectar();
			conexion.setAutoCommit(false);
			
			try (PreparedStatement psRegistros = conexion.prepareStatement(sqlRegistros)) {
                psRegistros.setLong(1, id);
                psRegistros.executeUpdate();
            }

            // --- Paso 2: Borrar la entidad en 'entidades' (y en Empleados/Temporales) ---
            try (PreparedStatement psEntidad = conexion.prepareStatement(sqlEntidad)) {
                psEntidad.setLong(1, id);
                int filasAfectadas = psEntidad.executeUpdate();
                
                // Si filasAfectadas es 0, el ID no existía.
                // Hacemos rollback por precaución, aunque el borrado
                // de registros no haya hecho nada.
                if (filasAfectadas == 0) {
                    throw new SQLException("El idEntidad " + id + " no existe.");
                }
            }

            // --- FIN DE LA TRANSACCIÓN: Confirmar ---
            conexion.commit();
            return true; // ¡Todo salió bien!

        } catch (SQLException e) {
            System.err.println("Error al eliminar la entidad y su historial: " + e.getMessage());
            
            // --- ERROR: Revertir ---
            try {
                if (conexion != null) {
                    System.err.println("Haciendo rollback de la transacción...");
                    conexion.rollback(); // Deshacemos cualquier cambio
                }
            } catch (SQLException ex) {
                System.err.println("Error crítico al hacer rollback: " + ex.getMessage());
            }
            return false; // Indica que la eliminación falló
            
        } finally {
            // Cerramos la conexión (esto también la pone en auto-commit de nuevo)
            try {
                if (conexion != null) {
                    conexion.setAutoCommit(true); // Devolvemos al estado normal
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
}