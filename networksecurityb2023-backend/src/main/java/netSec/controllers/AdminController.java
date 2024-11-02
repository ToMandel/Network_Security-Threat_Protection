package netSec.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import netSec.boundaries.UserBoundary;
import netSec.logic.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
public class AdminController {
	private UserService users;

	@Autowired
	public AdminController(UserService users) {
		this.users = users;
	}

// ADMIN TEST AND CONTROL HTTP REQUESTS

	@CrossOrigin(origins = "*")
	@RequestMapping(path = { "/users" }, //
			method = { RequestMethod.DELETE }) //
	public void deleteAllUsers() {
		this.users.deleteAllUsers();
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(path = { "/users" }, //
			method = { RequestMethod.GET }, //
			produces = { MediaType.APPLICATION_JSON_VALUE }) //
	public UserBoundary[] getAllUsers() {
		List<UserBoundary> rv = this.users.getAllUsers();
		return rv.toArray(new UserBoundary[0]);
	}
}
