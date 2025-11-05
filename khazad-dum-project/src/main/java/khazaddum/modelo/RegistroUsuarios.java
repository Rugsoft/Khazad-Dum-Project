package khazaddum.modelo;

import java.sql.Timestamp;

public record RegistroUsuarios(String fechaHora,
	    					   String tipoRegistro,
	                           String nombreCompleto) {}