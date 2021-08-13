package com.ppakgom.api.service;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.UserModifyInfoReq;
import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;

public interface UserService {

	User createUser(UserRegisterPostReq registerInfo, MultipartFile thumbnail);
	boolean checkName(String name);
	User getUserByUserId(String userId);
	User getUserById(Long userId);
	List<String> getInterest(Long userid);
	String modifyUserInfo(User user, UserModifyInfoReq userReq, MultipartFile file);
	void likeStudy(User user, Study study);
	void unlikeStudy(User user, Study study);
	User postSocialLoginInfo(String email);
	User getUserByEmail(String email);
	User getUserByName(String name);
}
