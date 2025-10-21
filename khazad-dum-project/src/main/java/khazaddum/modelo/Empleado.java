package khazaddum.modelo;

public class Empleado {

	 private String name;
	 private String lastName1;
	 private String lastName2;
	 private String dni;
	 private String genero;
	 private String puesto;
	 private String email;
	 private int nivelAcceso;
	 private byte[] foto;
	 
	 public Empleado(String name, String lastName1, String lastName2, String dni, String genero, String puesto,
			String email, int nivelAcceso, byte[] foto) {

		this.name = name;
		this.lastName1 = lastName1;
		this.lastName2 = lastName2;
		this.dni = dni;
		this.genero = genero;
		this.puesto = puesto;
		this.email = email;
		this.nivelAcceso = nivelAcceso;
		this.foto = foto;
	 }
	 
	 public Object[] crear() {
			return new Object[] {name, lastName1, lastName2, dni, genero, puesto, email, nivelAcceso, foto};
		}
	 

	 public String getName() {
		 return name;
	 }

	 public void setName(String name) {
		 this.name = name;
	 }

	 public String getLastName1() {
		 return lastName1;
	 }

	 public void setLastName1(String lastName1) {
		 this.lastName1 = lastName1;
	 }

	 public String getLastName2() {
		 return lastName2;
	 }

	 public void setLastName2(String lastName2) {
		 this.lastName2 = lastName2;
	 }

	 public String getDni() {
		 return dni;
	 }

	 public void setDni(String dni) {
		 this.dni = dni;
	 }

	 public String getGenero() {
		 return genero;
	 }

	 public void setGenero(String genero) {
		 this.genero = genero;
	 }

	 public String getPuesto() {
		 return puesto;
	 }

	 public void setPuesto(String puesto) {
		 this.puesto = puesto;
	 }

	 public String getEmail() {
		 return email;
	 }

	 public void setEmail(String email) {
		 this.email = email;
	 }

	 public int getNivelAcceso() {
		 return nivelAcceso;
	 }

	 public void setNivelAcceso(int nivelAcceso) {
		 this.nivelAcceso = nivelAcceso;
	 }

	 public byte[] getFoto() {
		 return foto;
	 }

	 public void setFoto(byte[] foto) {
		 this.foto = foto;
	 }
	 
	 
}
