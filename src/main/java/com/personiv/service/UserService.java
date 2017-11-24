package com.personiv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personiv.dao.UserDao;
import com.personiv.model.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public List<User> getNonAdminUsers(){
		return userDao.getNonAdminUsers();
	}

	public void addNonAdminUser(User user) {
		userDao.addNonAdminUser(user);	
	}

	public void resetPassword(User user) {
		userDao.resetPassword(user);
		
	}
}
