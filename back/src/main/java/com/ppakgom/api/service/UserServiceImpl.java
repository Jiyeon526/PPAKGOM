package com.ppakgom.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppakgom.db.entity.User;
import com.ppakgom.db.repository.UserRepository;

@Service("UserService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public User getUserByUserId(String userId) {
		return userRepository.findUserById(userId);
	
	}

}
