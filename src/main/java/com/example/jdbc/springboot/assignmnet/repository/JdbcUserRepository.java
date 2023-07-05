package com.example.jdbc.springboot.assignmnet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import com.example.jdbc.springboot.assignmnet.mapper.UserRowMapper;
import com.example.jdbc.springboot.assignmnet.model.Address;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import com.example.jdbc.springboot.assignmnet.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class JdbcUserRepository implements UserRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// TO CHECK WHETHER THE USER WITH SAME NAME AND MOBILENUMBER EXISTS
	@Override
	public boolean isUnique(String name, String mobileNumber) {
		String query = "SELECT COUNT(*) FROM users WHERE name = ? AND mobilenumber = ?";
		int count = jdbcTemplate.queryForObject(query, Integer.class, name, mobileNumber);
		return count == 0;
	}

	// TO CREATE A LIST OF USERS IF NOT EXISTED BEFORE
	@Override
	public List<String> save(List<User> userList) {
		List<String> errorMessages = new ArrayList<>();

		for (User user : userList) {
			String name = user.getName();
			String mobileNumber = user.getMobileNumber();

			// Check if the combination of name and mobile number already exists
			if (isUnique(name, mobileNumber)) {
				UUID uuid = UUID.randomUUID();
				String addressJson;
				RestTemplate restTemplate = new RestTemplate();
				String apiUrl = "https://random-data-api.com/api/v2/users?size=1";
				ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
				String responseBody = response.getBody();
				ObjectMapper objectMapper = new ObjectMapper();

				try {
					JsonNode rootNode = objectMapper.readTree(responseBody);
					JsonNode addressNode = rootNode.get("address");
					Address address = objectMapper.convertValue(addressNode, Address.class);
					user.setAddress(address);
					addressJson = objectMapper.writeValueAsString(user.getAddress());
					System.out.println(addressJson);
				} catch (JsonProcessingException e) {
					errorMessages.add("Failed to process JSON for user: " + user.getName());
					continue; // Move on to the next user
				}

				long currentTime = System.currentTimeMillis();
				user.setCreatedTime(currentTime);

				jdbcTemplate.update(
						"INSERT INTO users (id, name, gender, mobilenumber, address, isActive, createdTime) "
								+ "VALUES (?, ?, ?, ?, cast(? as jsonb), ?, ?)",
						new Object[] { uuid, user.getName(), user.getGender(), user.getMobileNumber(), addressJson,
								user.getIsActive(), currentTime });
			} else {
				String errorMessage = String.format(
						"User with the same name and mobile number already exists: Name: %s, Mobile Number: %s",
						user.getName(), user.getMobileNumber());
				errorMessages.add(errorMessage);
			}
		}

		return errorMessages;
	}

	// TO SEARCH FOR A USER WITH GIVEN ID AND ACTIVE STATUS
	@Override
	public User findByserachcriteria(UUID id, boolean isActive) {
		try {
			String sql = "SELECT * FROM users WHERE id=? AND isActive=?";
			List<User> users = jdbcTemplate.query(sql, new Object[] { id, isActive }, new UserRowMapper());
			if (!users.isEmpty()) {
				return users.get(0);
			} else
				return null;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	// TO GET ALL THE ACTIVE USERS
	@Override
	public List<User> findActiveUsers() {
		String sql = "SELECT * FROM active_users";
		return jdbcTemplate.query(sql, new UserRowMapper());
	}

	// TO GET ALL THE INACTIVE USERS
	@Override
	public List<User> findInactiveUsers() {
		String sql = "SELECT * FROM inactive_users";
		return jdbcTemplate.query(sql, new UserRowMapper());
	}

	// TO UPDATE A LIST OF USERS
	@Override
	public User update(User user) {
		StringBuilder query = new StringBuilder("UPDATE users SET");
		List<Object> params = new ArrayList<>();

		if (user.getName() != null) {
			query.append(" name = ?,");
			params.add(user.getName());
		}

		if (user.getGender() != null) {
			query.append(" gender = ?,");
			params.add(user.getGender());
		}

		if (user.getMobileNumber() != null) {
			query.append(" mobilenumber = ?,");
			params.add(user.getMobileNumber());
		}

		query.deleteCharAt(query.length() - 1);

		query.append(" WHERE id = ? AND isActive = ?");
		params.add(user.getId());
		params.add(user.getIsActive());

		jdbcTemplate.update(query.toString(), params.toArray());
		// Retrieve the updated user from the database
		User updatedUser = findByserachcriteria(user.getId(), user.getIsActive());
		updatedUser.setAddress(user.getAddress()); // Set the original address
		updatedUser.setCreatedTime(user.getCreatedTime()); // Set the original createdTime

		return updatedUser;
	}

	// TO DELETE A USER WITH GIVEN ID AND ACTIVE STATUS
	@Override
	public int deleteBysearchcriteria(UUID id, boolean isActive) {
		return jdbcTemplate.update("DELETE FROM users WHERE id=? AND isActive=?", id, isActive);
	};

	// TO GET ALL THE USERS
	@Override
	public List<User> findAll() {
		String sql = "SELECT * FROM users";
		return jdbcTemplate.query(sql, new UserRowMapper());
	}

	// TO DELETE ALL THE USERS
	@Override
	public int deleteAll() {
		return jdbcTemplate.update("DELETE from users");
	}
}