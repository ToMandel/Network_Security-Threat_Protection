package netSec.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
public class UserEntity {
	@Id
	private String id;
	private String userName;
	private String password;
	private String email;
	private byte[] salt;
    
	@ElementCollection
    private List<String> passwordHistory;

	public UserEntity() {
		this.passwordHistory = new ArrayList<String>();
	}

	public List<String> getPasswordHistory() {
		return passwordHistory;
	}

	public void setPasswordHistory(String passwordHistory) {
		this.passwordHistory.add(passwordHistory);
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt2) {
		this.salt = salt2;
	}
	
	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", userName=" + userName + ", password=" + password + ", email=" + email
				+ ", salt=" + Arrays.toString(salt) + ", passwordHistory=" + passwordHistory + "]";
	}
}
