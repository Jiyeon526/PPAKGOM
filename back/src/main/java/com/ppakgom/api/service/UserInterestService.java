package com.ppakgom.api.service;

import java.util.List;

import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserInterest;

public interface UserInterestService {

	List<UserInterest> getInterestByUser(User user);

	List<UserInterest> findByInterestId(Long id);

	
}
