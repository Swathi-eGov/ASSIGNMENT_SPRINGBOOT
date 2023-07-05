package com.example.jdbc.springboot.assignmnet.repository;

import java.util.List;
import java.util.UUID;

import com.example.jdbc.springboot.assignmnet.model.User;

public interface UserRepository {

	boolean isUnique(String name, String mobileNumber);

	List<String> save(List<User> user);

	User update(User user);

	User findByserachcriteria(UUID id, boolean isActive);

	int deleteBysearchcriteria(UUID id, boolean isActive);

	List<User> findAll();

	List<User> findInactiveUsers();

	List<User> findActiveUsers();

	int deleteAll();
}