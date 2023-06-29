package com.example.jdbc.springboot.assignmnet.repository;


import java.util.List;

import com.example.jdbc.springboot.assignmnet.model.User;


public interface UserRepository {
  int save(User user);

  User update(User user);

  User findById(int id);

  int deleteById(int id);

  List<User> findAll();

  User findByMobileNumber(String mobilenumber);
  
  User findByIdAndMobileNumber(int id,String mobilenumber);

  int deleteAll();
}