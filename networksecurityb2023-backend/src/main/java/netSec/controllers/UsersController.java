package netSec.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import netSec.boundaries.UserBoundary;
import netSec.logic.UserService;

@RestController
public class UsersController {
	private UserService users;

	@Autowired
	public UsersController(UserService users) {
		this.users = users;
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = { "/users" }, //
			method = { RequestMethod.POST }, //
			produces = { MediaType.APPLICATION_JSON_VALUE }, //
			consumes = { MediaType.APPLICATION_JSON_VALUE }) //
	public UserBoundary createUser(@RequestBody UserBoundary user) {
		return this.users.createUser(user);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = { "/users" }, //
			method = { RequestMethod.PUT }, //
			consumes = { MediaType.APPLICATION_JSON_VALUE }) //
	public void updateUserPass( //
			@RequestBody UserBoundary update) {
		this.users.updatePass(update);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = { "/users/forgot" }, //
			method = { RequestMethod.PUT }, //
			consumes = { MediaType.APPLICATION_JSON_VALUE }) //
	public void updateUserPassWithoutLogin( //
			@RequestBody UserBoundary user) {
		this.users.updateUser(user);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = { "/users/login" }, //
			method = { RequestMethod.POST }, //
			produces = { MediaType.APPLICATION_JSON_VALUE }, //
			consumes = { MediaType.APPLICATION_JSON_VALUE }) //
	public UserBoundary login(@RequestBody UserBoundary user) {
		return this.users.login(user);
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = { "/users/forgot" }, //
			method = { RequestMethod.POST }, //
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public int forgotPass(@RequestBody UserBoundary user) throws NoSuchAlgorithmException {
		return this.users.forgotPass(user);
	}
}
