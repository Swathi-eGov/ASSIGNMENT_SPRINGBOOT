package com.example.jdbc.springboot.assignmnet.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import com.example.jdbc.springboot.assignmnet.model.Address;
import com.example.jdbc.springboot.assignmnet.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getObject("id", UUID.class));
		user.setName(rs.getString("name"));
		user.setGender(rs.getString("gender"));
		user.setMobileNumber(rs.getString("mobilenumber"));
		user.setIsActive(rs.getBoolean("isActive"));
		user.setCreatedTime(rs.getLong("createdTime"));

		String addressJson = rs.getString("address");
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			Address address = objectMapper.readValue(addressJson, Address.class);
			user.setAddress(address);
		} catch (JsonProcessingException e) {
			throw new SQLException("Failed to convert JSON to Address", e);
		}

		return user;
	}
}
