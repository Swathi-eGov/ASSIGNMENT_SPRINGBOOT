package com.example.jdbc.springboot.assignmnet.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.jdbc.springboot.assignmnet.model.Address;
import com.example.jdbc.springboot.assignmnet.model.User;
import com.example.jdbc.springboot.assignmnet.model.UserSearchCriteria;
import com.example.jdbc.springboot.assignmnet.producer.KafkaProducer;
import com.example.jdbc.springboot.assignmnet.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/example")

public class UserController {

	@Autowired
	UserRepository userRepository;

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Autowired
	public UserController(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

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
	public ResponseEntity<List<String>> createUser(@RequestBody List<User> users) {
		List<String> createStatus = new ArrayList<>();
		for (User user : users) {
			if (!(userRepository.isUnique(user.getName(), user.getMobileNumber()))) {
				createStatus.add("User with the same name " + user.getName() + " and mobile number "
						+ user.getMobileNumber() + " already exists");
			} else {
				try {
					RestTemplate restTemplate = new RestTemplate();
					String apiUrl = "https://random-data-api.com/api/v2/users?size=1";
					ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
					String addressJson = null;
					String apiResponse = response.getBody();
					{
						if (apiResponse != null) {
							JsonNode rootNode = objectMapper.readTree(apiResponse);
							JsonNode addressNode = rootNode.get("address");
							Address address = objectMapper.treeToValue(addressNode, Address.class);
							user.setAddress(address);
							System.out.println(user.getAddress().toString());
						}
					}
					kafkaProducer.userCreation(user);
					createStatus.add("User was created successfully with name " + user.getName());
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		return new ResponseEntity<>(createStatus, HttpStatus.CREATED);
	}

	@PutMapping("/_update")
	public ResponseEntity<List<String>> updateUsers(@RequestBody List<User> userList) {
		try {
			List<String> updateStatus = new ArrayList<>();

			for (User user : userList) {
				User existingUser = userRepository.findByserachcriteria(user.getId(), user.getIsActive());
				if (existingUser == null) {
					updateStatus.add("User with id " + user.getId() + " does not exist");
					continue;
				}
				kafkaProducer.userUpdation(user);
				updateStatus.add("User with id " + user.getId() + " was updated successfully");
			}

			return new ResponseEntity<>(updateStatus, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Cannot update users: " + e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/_deleteuser")
	public ResponseEntity<String> deleteUser(@RequestBody UserSearchCriteria userSearchCriteria) {
		String res = "";
		try {
			User existingUser = userRepository.findByserachcriteria(userSearchCriteria.getId(),
					userSearchCriteria.getIsActive());
			if (existingUser == null) {
				res = "User with ID " + userSearchCriteria.getId() + " does not exist";
				return new ResponseEntity<>(res, HttpStatus.OK);
			}

			kafkaProducer.userDeletion(userSearchCriteria);
			res = "Deleted user with ID " + userSearchCriteria.getId() + " successfully";
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Cannot delete user.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/_delete")
	public ResponseEntity<String> deleteAllUsers() {
		try {
			kafkaProducer.allUsersDeletion();
			return new ResponseEntity<>("Deleted all users successfully .", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Cannot delete users.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}