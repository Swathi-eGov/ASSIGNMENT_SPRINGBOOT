package com.example.jdbc.springboot.assignmnet.repository;

import java.util.List;
import java.util.UUID;

import com.example.jdbc.springboot.assignmnet.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserRepository {

	boolean isUnique(String name, String mobileNumber);

	int save(User user) throws JsonProcessingException;

	User update(User user);

	User findByserachcriteria(UUID id, boolean isActive);

	int deleteBysearchcriteria(UUID id, boolean isActive);

	List<User> findAll();

	List<User> findInactiveUsers();

	List<User> findActiveUsers();

	int deleteAll();
}