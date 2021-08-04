package com.ppakgom.api.service;

import com.ppakgom.db.entity.User;

public interface UserService {
	User getUserByUserId(String userId);
}
