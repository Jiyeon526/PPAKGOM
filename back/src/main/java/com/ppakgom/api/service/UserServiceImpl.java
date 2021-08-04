package com.ppakgom.api.service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserInterest;
import com.ppakgom.db.repository.InterestRepository;
import com.ppakgom.db.repository.UserInterestRepository;
import com.ppakgom.db.repository.UserRepository;

@Service("UserService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	InterestRepository interestRepository;
	
	@Autowired
	UserInterestRepository userInterestRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	String BASE_PATH = "../../../../../resources/image/";
	
	@Override
	public User createUser(UserRegisterPostReq registerInfo, MultipartFile thumbnail) { // 사용자 회원가입
		
		try {
			// 사용자 정보 저장
			User user = new User();
			user.setUserId(registerInfo.getUserId());
			user.setPassword(passwordEncoder.encode(registerInfo.getPassword()));
			user.setEmail(registerInfo.getEmail());
			user.setName(registerInfo.getName());
			user.setTemperature(36.5f);
			user.setPlatform_type("none");
			user.setAccess_token("none");
			
			/* 프로필 이미지 저장
			 닉네임-파일명으로 저장 */
			String filePath = BASE_PATH + "user" + user.getName() + "-" + registerInfo.getProfileThumbnail();
			File dest = new File(filePath);
			thumbnail.transferTo(dest);
			
			if(!dest.exists()) { // 파일 존재 x, 이 부분이 현재 null 값이 들어갈 수 있음, 추후 다시 해야됨
	            System.out.println("파일 업로드 실패");
	        }else {
	        	user.setProfile_thumbnail(filePath);
	        }
			
			// user 정보 insert
			userRepository.save(user);
			
			// 회원 관심사 정보
			for(String s : registerInfo.getInterest()) {
				s = s.replaceAll(" ", ""); // 해시태그 공백제거
				Optional<Interest> exist = interestRepository.findByName(s); // 해당 단어가 관심사 테이블에 있는지 확인
				if(!exist.isPresent()) { // 만약 관심사가 존재안한다면
					interestRepository.save(s); // 관심사 insert
					exist = interestRepository.findByName(s); // 다시 찾기
				}
				
				UserInterest userInterest = new UserInterest(user, exist.get()); // 유저관심사 테이블에 저장
				userInterestRepository.save(userInterest);
			}
			
			return user;
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean checkDuplicateName(String name) {
		Optional<User> user = userRepository.findByName(name);
		
		if(user.isPresent())
			return false;
		return true;
	}

}
