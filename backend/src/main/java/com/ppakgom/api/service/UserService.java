package com.ppakgom.api.service;

import java.util.ArrayList;

import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.api.response.ConferenceUsers;
import com.ppakgom.db.entity.User;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserService {
	User createUser(UserRegisterPostReq userRegisterInfo);
	//User modifyUser(UserRegisterPostReq userModifyInfo);
	//User deleteUser(String userId);
	User getUserByUserId(String userId);
	void updateUserInfo(String userId, UserRegisterPostReq userRegisterInfo);
	void deleteUserInfo(String userId);
	boolean checkDuplicateId(String userId);
	ArrayList<ConferenceUsers> getConferenceUserInfo(Long id);


}
