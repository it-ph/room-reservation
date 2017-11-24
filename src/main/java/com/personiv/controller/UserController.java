package com.personiv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personiv.model.User;
import com.personiv.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(path= "/user/getNonAdminUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getNonAdminUsers(){
		return userService.getNonAdminUsers();
	}
	
	@RequestMapping(path= "/user/addNonAdminUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addNonAdminUsers(@RequestBody User user){
		userService.addNonAdminUser(user);
		return ResponseEntity.ok(user);
	}
	@RequestMapping(path= "/user/resetPassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	
	public ResponseEntity<?> resetPasdword(@RequestBody User user){
		userService.resetPassword(user);
		return ResponseEntity.ok(user);
	}
}
