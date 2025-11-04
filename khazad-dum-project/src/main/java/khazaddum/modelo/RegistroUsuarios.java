package khazaddum.modelo;

import java.sql.Timestamp;

public record RegistroUsuarios(Timestamp fechaHora,
	    					   String tipoRegistro,
	                           String nombreCompleto) {}
