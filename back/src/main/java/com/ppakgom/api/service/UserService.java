package com.ppakgom.api.service;

import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.db.entity.User;

public interface UserService {

	User createUser(UserRegisterPostReq registerInfo, MultipartFile thumbnail);
	
}
