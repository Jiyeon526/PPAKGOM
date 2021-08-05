package com.ppakgom.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.db.entity.User;

public interface UserService {

	User createUser(UserRegisterPostReq registerInfo, MultipartFile thumbnail);
	boolean checkName(String name);
	User getUserByUserId(String userId);
	User getUserById(Long userId);

}
