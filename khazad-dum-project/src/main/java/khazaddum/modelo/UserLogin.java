package khazaddum.modelo;

public class UserLogin {

	private String name;
	private String lastName;
	private String user;
	private String password;
	private String email;
	private String role;
	
	public UserLogin(String name, String lastName, String user, String password, String email, String role) {
		this.name = name;
		this.lastName = lastName;
		this.user = user;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public Object[] crear() {
		return new Object[] {name, lastName, user, password, email, role};
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
}
