package khazaddum.modelo;

import java.io.File;

/**
 * Representa una visita temporal (usuario visitante) del sistema.
 * <p>
 * Contiene información básica del visitante temporal como identificador,
 * nombre, apellidos, DNI, motivo de la visita, foto, duración en horas,
 * código de tag RFID y una representación de la fecha de expiración.
 * </p>
 */
public class VisitaTemporal {

	private int idVisita;
	private String name;
	private String lastName1;
	private String lastName2;
	private String dni;
	private String motivo;
	private File foto;
	private int horas;
	private String codigoTag;
	private String fechaString;
	
	/**
	 * Construye una nueva instancia de VisitaTemporal con todos sus campos.
	 *
	 * @param idVisita identificador de la visita
	 * @param name nombre del visitante
	 * @param lastName1 primer apellido
	 * @param lastName2 segundo apellido
	 * @param dni documento nacional de identidad
	 * @param motivo motivo de la visita
	 * @param foto archivo con la foto (puede ser {@code null})
	 * @param horas duración en horas del permiso
	 * @param codigoTag código RFID asociado
	 * @param fechaString representación textual de la fecha de expiración
	 */
	public VisitaTemporal(int idVisita, String name, String lastName1, String lastName2, String dni, String motivo, File foto,
			int horas, String codigoTag, String fechaString ) {
	
		this.idVisita = idVisita;
		this.name = name;
		this.lastName1 = lastName1;
		this.lastName2 = lastName2;
		this.dni = dni;
		this.motivo = motivo;
		this.foto = foto;
		this.horas = horas;
		this.codigoTag = codigoTag;
		this.fechaString = fechaString;
	}
	
	/**
	 * Devuelve una representación en forma de array de objetos con los campos
	 * de la visita temporal, útil para modelos de tablas.
	 *
	 * @return array de objetos con los datos de la visita temporal.
	 */
	public Object[] crear() {
		return new Object[] {idVisita, name, lastName1, lastName2, dni, motivo, foto, horas, codigoTag, fechaString};
	}

	/**
	 * @return el identificador de la visita temporal
	 */
	public int getIdVisita() {
		return idVisita;
	}

	/**
	 * @param idVisita establece el identificador de la visita temporal
	 */
	public void setIdVisita(int idVisita) {
		this.idVisita = idVisita;
	}
	
	
}