package com.example.jdbc.springboot.assignmnet.repository;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.jdbc.springboot.assignmnet.model.User;




@Repository
public class JdbcUserRepository implements UserRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public int save(User user) {
    return jdbcTemplate.update("INSERT INTO person (id,name,gender,mobilenumber,address) VALUES(?,?,?,?,?)",
        new Object[] { user.getId(),user.getName(), user.getGender(), user.getMobileNumber(),user.getAddress() });
  }

  @Override
  public User update(User user){
      StringBuilder query = new StringBuilder("UPDATE person SET");
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

      if (user.getAddress() != null) {
          query.append(" address = ?,");
          params.add(user.getAddress());
      }
      
      query.deleteCharAt(query.length() - 1);

      query.append(" WHERE id = ?");
      params.add(user.getId());

      jdbcTemplate.update(query.toString(), params.toArray());
      return user;
  }

  @Override
  public User findById(int id) {
    try {
      User user = jdbcTemplate.queryForObject("SELECT * FROM person WHERE id=?",
          BeanPropertyRowMapper.newInstance(User.class), id);

      return user;
    } catch (IncorrectResultSizeDataAccessException e) {
      return null;
    }
  }

  @Override
  public int deleteById(int id) {
    return jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
  }

  @Override
  public List<User> findAll() {
    return jdbcTemplate.query("SELECT * from person", BeanPropertyRowMapper.newInstance(User.class));
  }

  @Override
  public User findByMobileNumber(String mobileNumber) {
    return jdbcTemplate.queryForObject("SELECT * from person WHERE mobilenumber=?",
        BeanPropertyRowMapper.newInstance(User.class), mobileNumber);
  }

  
  @Override
  public User findByIdAndMobileNumber(int id,String mobileNumber) {
    return jdbcTemplate.queryForObject("SELECT * from person WHERE id=? AND mobilenumber=?",
        BeanPropertyRowMapper.newInstance(User.class), id,mobileNumber);
  }


  @Override
  public int deleteAll() {
    return jdbcTemplate.update("DELETE from person");
  }
}