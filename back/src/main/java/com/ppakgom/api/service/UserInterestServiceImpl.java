package com.ppakgom.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserInterest;
import com.ppakgom.db.repository.UserInterestRepository;

@Service("UserInterstService")
public class UserInterestServiceImpl implements UserInterestService {
	
	@Autowired
	UserInterestRepository userInterestRepository;
	
	@Override
	public List<UserInterest> getInterestByUser(User user) {
		return userInterestRepository.findInterestByUserId(user.getId());
	}

}
