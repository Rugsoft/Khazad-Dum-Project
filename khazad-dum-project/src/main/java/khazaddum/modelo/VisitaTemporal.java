package khazaddum.modelo;

import java.io.File;

public class VisitaTemporal {

	private String name;
	private String lastName1;
	private String lastName2;
	private String dni;
	private String motivo;
	private File foto;
	private int horas;
	
	public VisitaTemporal(String name, String lastName1, String lastName2, String dni, String motivo, File foto,
			int horas) {
	
		this.name = name;
		this.lastName1 = lastName1;
		this.lastName2 = lastName2;
		this.dni = dni;
		this.motivo = motivo;
		this.foto = foto;
		this.horas = horas;
	}
	
	public Object[] crear() {
		return new Object[] {name, lastName1, lastName2, dni, motivo, foto, horas};
	}
	
	
}
