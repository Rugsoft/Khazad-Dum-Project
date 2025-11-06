package khazaddum.modelo;

/**
 * Representa las credenciales y datos básicos de un usuario para inicio de sesión.
 * <p>
 * Contiene campos para nombre, apellidos, usuario, contraseña, correo y rol.
 * Proporciona métodos para construir la representación que se usa al persistir
 * en la base de datos.
 * </p>
 */
public class UserLogin {

	private String name;
	private String lastName;
	private String user;
	private String password;
	private String email;
	private String role;
	
	/**
	 * Construye un nuevo objeto UserLogin con los datos proporcionados.
	 *
	 * @param name nombre
	 * @param lastName apellidos
	 * @param user nombre de usuario
	 * @param password contraseña
	 * @param email correo electrónico
	 * @param role rol/nivel de acceso
	 */
	public UserLogin(String name, String lastName, String user, String password, String email, String role) {
		this.name = name;
		this.lastName = lastName;
		this.user = user;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	/**
	 * Devuelve un array de objetos con los valores del usuario en el orden
	 * esperado por las consultas de inserción.
	 *
	 * @return array con los campos del usuario
	 */
	public Object[] crear() {
		return new Object[] {name, lastName, user, password, email, role};
	}
	
	/** @return el nombre */
	public String getName() {
		return name;
	}

	/** @param name establece el nombre */
	public void setName(String name) {
		this.name = name;
	}

	/** @return los apellidos */
	public String getLastName() {
		return lastName;
	}

	/** @param lastName establece los apellidos */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/** @return el nombre de usuario */
	public String getUser() {
		return user;
	}

	/** @param user establece el nombre de usuario */
	public void setUser(String user) {
		this.user = user;
	}

	/** @return la contraseña */
	public String getPassword() {
		return password;
	}

	/** @param password establece la contraseña */
	public void setPassword(String password) {
		this.password = password;
	}

	/** @return el correo electrónico */
	public String getEmail() {
		return email;
	}

	/** @param email establece el correo */
	public void setEmail(String email) {
		this.email = email;
	}

	/** @return el rol/nivel de acceso */
	public String getRole() {
		return role;
	}

	/** @param role establece el rol/nivel de acceso */
	public void setRole(String role) {
		this.role = role;
	}
	
	
}