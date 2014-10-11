package com.jimmystudio.movietime_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jimmystudio.movietime_service.entity.User;

//@RestController
//@RequestMapping(value="/users")
public class RootController {
/*

	@Autowired
	JpaRepository<User, Long> userRepository;
	
	@RequestMapping(value="/{userID}", method=RequestMethod.GET)
    public User getUser(@PathVariable Long userID) {

		return userRepository.findOne(userID);
		
    }
	
	@RequestMapping(method=RequestMethod.GET)
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	@RequestMapping(value="/init", method=RequestMethod.GET)
	public void initUsers(){
		User user = new User();
		user.setName("Jimmy");
		user.setPassword("password");
		userRepository.save(user);
	}*/
	
}
