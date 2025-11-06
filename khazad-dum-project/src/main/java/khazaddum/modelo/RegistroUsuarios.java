package khazaddum.modelo;

/**
 * Representa un registro de entrada/salida de un usuario.
 * <p>
 * Este record contiene la marca temporal formateada, el tipo de registro
 * (entrada/salida) y el nombre completo del usuario. Se utiliza para mostrar
 * y transportar los datos de registros en las vistas y tablas.
 * </p>
 *
 * @param fechaHora    cadena formateada con la fecha y hora del registro
 * @param tipoRegistro tipo de registro (por ejemplo "entrada" o "salida")
 * @param nombreCompleto nombre completo del usuario asociado al registro
 */
public record RegistroUsuarios(String fechaHora,
	    					   String tipoRegistro,
	                           String nombreCompleto) {}