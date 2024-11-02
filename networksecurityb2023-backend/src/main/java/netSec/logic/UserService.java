package netSec.logic;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import netSec.boundaries.UserBoundary;

public interface UserService {

	// Users
	public UserBoundary createUser(UserBoundary user);

	public void updateUser(UserBoundary update);
	
	public void updatePass(UserBoundary user);

	public UserBoundary login(UserBoundary user);

	public int forgotPass(UserBoundary user) throws NoSuchAlgorithmException;

	// ADMIN
	public void deleteAllUsers();

	public List<UserBoundary> getAllUsers();

}
