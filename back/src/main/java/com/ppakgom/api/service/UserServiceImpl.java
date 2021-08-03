package com.ppakgom.api.service;

import java.util.ArrayList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.api.response.ConferenceUsers;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserConference;
import com.ppakgom.db.repository.ConferenceHistoryRepository;
import com.ppakgom.db.repository.ConferenceRepository;
import com.ppakgom.db.repository.UserConferenceRepository;
import com.ppakgom.db.repository.UserRepository;
//import com.ppakgom.db.repository.UserRepositorySupport;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	
//	@Autowired
//	UserRepositorySupport userRepositorySupport;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserConferenceRepository userConferenceRepository;
	
	
//	@Autowired
//	ConferenceRepository conferenceRepository;
//
//	@Autowired
//	ConferenceHistoryRepository conferenceHistoryRepository;
//
//	@Autowired
//	UserConferenceRepository userConferenceRepository;

	
	@Override
	public User createUser(UserRegisterPostReq userRegisterInfo) {
		User user = new User();
		user.setDepartment(userRegisterInfo.getDepartment());
		user.setPosition(userRegisterInfo.getPosition());
		user.setName(userRegisterInfo.getName());
		user.setUserId(userRegisterInfo.getId());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User getUserByUserId(String userId) {
		// 디비에 유저 정보 조회 (userId 를 통한 조회).
//		User user = userRepositorySupport.findUserByUserId(userId).get();
		User user = null;
		return user;
	}
	
	@Transactional
	@Override
	public void deleteUserInfo(String userId) {
		User user = getUserByUserId(userId);

//		conferenceHistoryRepository.deleteByUserId(user.getId());
//		userConferenceRepository.deleteAllByUserId(user.getId());
//		conferenceRepository.deleteAllByUserId(user.getId());
		userRepository.delete(user);
	}
	
	@Override
	public void updateUserInfo(String userId, UserRegisterPostReq registerInfo) {
		User user = getUserByUserId(userId);
		user.setDepartment(registerInfo.getDepartment());
		user.setName(registerInfo.getName());
		user.setPosition(registerInfo.getPosition());
		userRepository.save(user);
	}

	@Override
	public boolean checkDuplicateId(String userId) {
		return userRepository.existsByUserId(userId);
	}

	@Override
	public ArrayList<ConferenceUsers> getConferenceUserInfo(Long conference_id) {
		Optional<UserConference[]> users = userConferenceRepository.findByConference_Id(conference_id);
		ArrayList<ConferenceUsers> res = new ArrayList<>();
				
		if(!users.isPresent())
			return null;
		for(UserConference u: users.get()) {
			Optional<User> user = userRepository.findById(u.getUser().getId());
			if(!user.isPresent()) continue;
			res.add(ConferenceUsers.of(user.get()));
		}
		
		return res;
	}

//	@Override
//	public User modifyUser(UserRegisterPostReq userModifyInfo) {
//		return null;
//	}

}
