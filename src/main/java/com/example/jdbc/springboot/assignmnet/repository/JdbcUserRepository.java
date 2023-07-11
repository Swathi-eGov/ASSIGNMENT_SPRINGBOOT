package com.example.jdbc.springboot.assignmnet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import com.example.jdbc.springboot.assignmnet.mapper.UserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.jdbc.springboot.assignmnet.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class JdbcUserRepository implements UserRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	ObjectMapper objectMapper;

	// TO CHECK WHETHER THE USER WITH SAME NAME AND MOBILENUMBER EXISTS
	@Override
	public boolean isUnique(String name, String mobileNumber) {
		String query = "SELECT COUNT(*) FROM users WHERE name = ? AND mobilenumber = ?";
		int count = jdbcTemplate.queryForObject(query, Integer.class, name, mobileNumber);
		return count == 0;
	}

	// TO CREATE A LIST OF USERS IF NOT EXISTED BEFORE
	@Override
	public int save(User user) throws JsonProcessingException {
		String addressJson = objectMapper.writeValueAsString(user.getAddress());
		System.out.println(addressJson);
		return jdbcTemplate.update(
				"INSERT INTO users (name, gender, mobileNumber, address, isActive) VALUES( ?, ?, ?, ?::json, ?)",
				user.getName(), user.getGender(), user.getMobileNumber(), addressJson, user.getIsActive());
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
		return jdbcTemplate.update("DELETE FROM users WHERE id = ? AND isActive = ?", id, isActive);

	}

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