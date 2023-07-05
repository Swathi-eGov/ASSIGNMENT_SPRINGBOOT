package com.example.jdbc.springboot.assignmnet.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.jdbc.springboot.assignmnet.model.User;
import com.example.jdbc.springboot.assignmnet.model.UserSearchCriteria;
import com.example.jdbc.springboot.assignmnet.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/example")

public class UserController {

	@Autowired
	UserRepository userRepository;

	@GetMapping("/_search")
	public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) boolean isActive) {
		try {
			List<User> users = new ArrayList<User>();
			userRepository.findAll().forEach(users::add);
			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/_searchuser")
	public ResponseEntity<User> getUser(@RequestBody UserSearchCriteria usersearchcriteria) {
		User user = userRepository.findByserachcriteria(usersearchcriteria.getId(), usersearchcriteria.getIsActive());
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(user);
	}

	@GetMapping("/active-users")
	public ResponseEntity<List<User>> getActiveUsers() {
		try {
			List<User> activeUsers = userRepository.findActiveUsers();
			if (activeUsers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(activeUsers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/inactive-users")
	public ResponseEntity<List<User>> getInactiveUsers() {
		try {
			List<User> inactiveUsers = userRepository.findInactiveUsers();
			if (inactiveUsers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(inactiveUsers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/_create")
	public ResponseEntity<?> createUsers(@RequestBody List<User> usersList) {
		List<String> errorMessages = userRepository.save(usersList);

		if (errorMessages.isEmpty()) {
			return new ResponseEntity<>("Users were created successfully.", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/_update")
	public ResponseEntity<List<User>> updateUsers(@RequestBody List<User> userList) {
		try {
			List<User> updatedUsers = new ArrayList<>();

			for (User user : userList) {
				User existingUser = userRepository.findByserachcriteria(user.getId(), user.getIsActive());
				if (existingUser == null) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				User updatedUser = userRepository.update(user);
				updatedUsers.add(updatedUser);
			}

			return new ResponseEntity<>(updatedUsers, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Cannot update users: " + e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/_deleteuser")
	public ResponseEntity<String> deleteUser(@RequestBody UserSearchCriteria usersearchcriteria) {
		try {
			int result = userRepository.deleteBysearchcriteria(usersearchcriteria.getId(),
					usersearchcriteria.getIsActive());
			if (result == 0) {
				return new ResponseEntity<>("Cannot find User with id=" + usersearchcriteria.getId(), HttpStatus.OK);
			}
			return new ResponseEntity<>("User was deleted successfully.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Cannot delete user.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/_delete")
	public ResponseEntity<String> deleteAllUsers() {
		try {
			int numRows = userRepository.deleteAll();
			return new ResponseEntity<>("Deleted " + numRows + " User(s) successfully.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Cannot delete users.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}