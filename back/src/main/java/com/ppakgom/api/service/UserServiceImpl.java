package com.ppakgom.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.ppakgom.db.entity.User;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

	String BASE_PATH = System.getProperty("user.dir");

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

			/*
			 * 프로필 이미지 저장 닉네임-파일명으로 저장
			 */
			BASE_PATH += "\\src\\main\\resources\\image";
			String filePath = BASE_PATH + "\\user\\" + user.getName() + "-" + thumbnail.getOriginalFilename();
			File dest = new File(filePath);
			thumbnail.transferTo(dest);

			if (!dest.exists()) { // 파일 존재 x, 이 부분이 현재 null 값이 들어갈 수 있음, 추후 다시 해야됨
				System.out.println("파일 업로드 실패");
			} else {
				user.setProfile_thumbnail(filePath);
			}

			// user 정보 insert
			userRepository.save(user);

			// 회원 관심사 정보
			for (String s : registerInfo.getInterest()) {
				s = s.replaceAll(" ", ""); // 해시태그 공백제거
				Interest exist = interestRepository.findByInterest(s); // 해당 단어가 관심사 테이블에 있는지 확인
				if (exist == null) { // 만약 관심사가 존재안한다면
					Interest interest = new Interest(s);
					interestRepository.save(interest); // 관심사 insert
					exist = interestRepository.findByInterest(s); // 다시 찾기
				}

				UserInterest userInterest = new UserInterest(user, exist); // 유저관심사 테이블에 저장
				userInterestRepository.save(userInterest);
			}

			return user;
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean checkName(String name) {
		Optional<User> user = userRepository.findByName(name);

		if (user.isPresent())
			return false;
		return true;
	}

	@Override
	public User getUserByUserId(String userId) {
		return userRepository.findUserById(userId);
	}

	public List<String> getInterest(Long userid) {
		List<Long> interestIds = userInterestRepository.findByInterestId(userid);
		List<String> res = new ArrayList<>();

		for (Long id : interestIds) {
			Interest name = interestRepository.findByName(id);
			res.add(name.getName());
		}

		return res;
	}

	@Override
	public User getUserById(Long userId) {
		return userRepository.findUserById(userId);
	}

}
