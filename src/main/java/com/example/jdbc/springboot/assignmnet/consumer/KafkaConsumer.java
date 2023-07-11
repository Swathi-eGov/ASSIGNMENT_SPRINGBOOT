package com.example.jdbc.springboot.assignmnet.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.jdbc.springboot.assignmnet.model.User;
import com.example.jdbc.springboot.assignmnet.model.UserSearchCriteria;
import com.example.jdbc.springboot.assignmnet.repository.JdbcUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaConsumer {

	private final JdbcUserRepository userRepository;

	@Autowired
	public KafkaConsumer(JdbcUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@KafkaListener(topics = "user-create-topic", groupId = "my-group-id")
	public void receiveCreateUser(String userString) {
		try {
			System.out.println("Received create string user request: " + userString);
			User user = new ObjectMapper().readValue(userString, User.class);
			userRepository.save(user);
			System.out.println("User saved successfully: " + user.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = "user-update-topic", groupId = "my-group-id")
	public void receiveUpdateUser(String userString) {
		try {
			System.out.println("Received update user request: " + userString);
			User user = new ObjectMapper().readValue(userString, User.class);
			userRepository.update(user);
			System.out.println("User updated successfully: " + user.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = "user-delete-topic", groupId = "my-group-id")
	public void receiveDeleteUser(String userSearchCriteriaString) {
		try {
			System.out.println("Received delete user request: " + userSearchCriteriaString);
			UserSearchCriteria userSearchCriteria = new ObjectMapper().readValue(userSearchCriteriaString,
					UserSearchCriteria.class);
			userRepository.deleteBysearchcriteria(userSearchCriteria.getId(), userSearchCriteria.getIsActive());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = "user-deleteAll-topic", groupId = "my-group-id")
	public void receiveDeleteAllUsers(String payload) {
		try {
			userRepository.deleteAll();
			System.out.println("Deleted User(s) successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
