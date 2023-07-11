package com.example.jdbc.springboot.assignmnet.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.jdbc.springboot.assignmnet.model.User;
import com.example.jdbc.springboot.assignmnet.model.UserSearchCriteria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void userCreation(User user) {
		try {
			String userJson = new ObjectMapper().writeValueAsString(user);
			kafkaTemplate.send("user-create-topic", userJson);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void userUpdation(User user) {
		try {
			String userJson = new ObjectMapper().writeValueAsString(user);
			kafkaTemplate.send("user-update-topic", userJson);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void userDeletion(UserSearchCriteria userSearchCriteria) {
		try {
			String userSearchCriteriaJson = new ObjectMapper().writeValueAsString(userSearchCriteria);
			kafkaTemplate.send("user-delete-topic", userSearchCriteriaJson);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void allUsersDeletion() {
		try {
			kafkaTemplate.send("user-deleteAll-topic", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
