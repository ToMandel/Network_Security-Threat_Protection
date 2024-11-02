package netSec.boundaries;

public class UserBoundary {
	private String id;
	private String userName;
	private String password;
	private String email;
	private String newPass;

	public UserBoundary() {
	}

	public UserBoundary(String id, String userName, String password, String email,String newPass) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.newPass = newPass;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", email=" + email + "]";
	}

	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}

}
