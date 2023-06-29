package com.example.jdbc.springboot.assignmnet.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jdbc.springboot.assignmnet.model.User;
import com.example.jdbc.springboot.assignmnet.model.UserSearchCriteria;
import com.example.jdbc.springboot.assignmnet.repository.UserRepository;


@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/example")

public class UserController {

  @Autowired
  UserRepository userRepository;

  @GetMapping("/_searchusers")
  public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String mobileNumber) {
    try {
      List<User> users = new ArrayList<User>();

      if (mobileNumber == null)
        userRepository.findAll().forEach(users::add);
      else
        userRepository.findByMobileNumber(mobileNumber);

      if (users.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(users, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/_searchbyid/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
    User user = userRepository.findById(id);

    if (user != null) {
      return new ResponseEntity<>(user, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  
  @GetMapping(value = "/_searchbyidandmobilenumber")
  public ResponseEntity<User> searchUser(@RequestBody UserSearchCriteria userSearchCriteria){
      try{
          User user;

          if(userSearchCriteria.getId() != 0){
              user = userRepository.findById(userSearchCriteria.getId());
          }
         
          user = userRepository.findByMobileNumber(userSearchCriteria.getMobileNumber());    
     
          if(user == null){
              System.out.println("User not found");
              return null;
          }

          return new ResponseEntity<User>(user,HttpStatus.OK);
      }catch (Exception e){
          System.out.println("Error in searching the user: "+ e.toString());
          return null;
      }
  }
  
  
  @PostMapping("/_createuser")
  public ResponseEntity<String> createUser(@RequestBody User user) {
    try {
      userRepository.save(new User (user.getId(),user.getName(), user.getGender(), user.getMobileNumber(),user.getAddress()));
      return new ResponseEntity<>("User was created successfully.", HttpStatus.CREATED);
    } catch (Exception e) {
    	e.printStackTrace();
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  
  @PutMapping("/_updateuser")
  public ResponseEntity<User> updateUser(@RequestBody User user){
      try{
          User _User = userRepository.findById(user.getId());

          if(_User == null){
              System.out.println("User not found");
              return null;
          }
          User user1 = userRepository.update(user);
          return new ResponseEntity<User>(user1,HttpStatus.OK);
      }catch (Exception e){
          System.out.println("Cannot update user: " + e.toString());
          return new ResponseEntity<User>(new User(),HttpStatus.NOT_FOUND);
      }
  }
  
  

  @DeleteMapping("/_deleteuserbyid/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable("id") int id) {
    try {
      int result = userRepository.deleteById(id);
      if (result == 0) {
        return new ResponseEntity<>("Cannot find User with id=" + id, HttpStatus.OK);
      }
      return new ResponseEntity<>("User was deleted successfully.", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Cannot delete user.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/_deleteusers")
  public ResponseEntity<String> deleteAllUsers() {
    try {
      int numRows = userRepository.deleteAll();
      return new ResponseEntity<>("Deleted " + numRows + " User(s) successfully.", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Cannot delete users.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

}