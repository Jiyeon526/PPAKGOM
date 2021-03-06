package com.ppakgom.api.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.ppakgom.db.entity.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.UserModifyInfoReq;
import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.UserInterest;
import com.ppakgom.db.entity.UserLikeStudy;
import com.ppakgom.db.repository.InterestRepository;
import com.ppakgom.db.repository.UserInterestRepository;
import com.ppakgom.db.repository.UserLikeStudyRepository;
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

	String BASE_PATH = System.getProperty("user.dir") + "image/user/";
	
	@Autowired
	UserLikeStudyRepository userLikeStudyRepository;
	
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
			user.setPosition("user");
			
//			유저 아이디 미리 뽑아두기
			User userTmp = userRepository.save(user);
			Long userId = userTmp.getId();
			
			/*
			 * 프로필 이미지 저장 순번-파일명으로 저장
			 */
			if(thumbnail == null) { // 프로필 사진 입력 안함
				//프로필 이미지 저장 (기본 이미지) 
				String filePath = BASE_PATH + "default.png";
				user.setProfile_thumbnail(filePath);
				
			} else { // 프로필 사진 입력함 
				String filePath = BASE_PATH + userId + "-" + thumbnail.getOriginalFilename();
				File dest = new File(filePath);
				thumbnail.transferTo(dest);

				if (!dest.exists()) { // 파일 존재 x, 이 부분이 현재 null 값이 들어갈 수 있음, 추후 다시 해야됨
					System.out.println("파일 업로드 실패");
				} else {
					user.setProfile_thumbnail(filePath);
				}
			}
			
			// user 정보 insert
			userRepository.save(user);

			// 회원 관심사 정보
			if(registerInfo.getInterest() == null) return user;
			insertUserInterest(user, registerInfo.getInterest());

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
		
		if(interestIds == null) return null;

		for (Long id : interestIds) {
			Interest name = interestRepository.findByName(id);
			res.add(name.getName());
		}

		return res;
	}

	@Override
	public User getUserById(Long userId) { // pk로 사용자 찾기
		Optional<User> user = userRepository.findById(userId);
		
		if(!user.isPresent()) // 사용자가 없다면
			return null;
		return user.get();
	}
	
	@Override
	public String modifyUserInfo(User user, UserModifyInfoReq userReq, MultipartFile file) {

		try {
			if(!checkName(userReq.getName()) && !user.getName().equals(userReq.getName())) // 닉네임 중복
				return "name";
			
			// 닉네임 바꾸기
			user.setName(userReq.getName());
			
			// 관심사 바꾸기
			// 현재 관심사 리스트 가져오기
			List<Long> interestIds = userInterestRepository.findByInterestId(user.getId());
			
			if(userReq.getInterest() != null) { // 입력된 관심사가 있을 때만 관심사 등록
				
				if(interestIds != null) { // 있다면 원래 내용 삭제
					deleteUserInterest(interestIds, user);
				}
				insertUserInterest(user, userReq.getInterest()); // 관심사 등록
				
			} else { // 원래 관심사 지워주기
				deleteUserInterest(interestIds, user);
			}
			
			// 원래 이미지 삭제
			// default 이미지면 삭제안하기
			if(user.getProfile_thumbnail().equals(BASE_PATH + "default.png")) {
				if(file != null) { // 입력 파일이 있을 경우 넣어주기
					// 이미지 삽입
					String filePath = BASE_PATH + user.getId() + "-" + file.getOriginalFilename();
					File dest = new File(filePath);
					file.transferTo(dest);
					
					if(!dest.exists()) { // 파일 존재 x, 이 부분이 현재 null 값이 들어갈 수 있음, 추후 다시 해야됨
			            System.out.println("파일 업로드 실패");
			        }else {
			        	user.setProfile_thumbnail(filePath);
			        }
				}
				// 입력 파일 없으면 그대로
			} else { // 원래 파일이 있다면 지워주기
				Path deleteFilePath = Paths.get(user.getProfile_thumbnail());
				Files.deleteIfExists(deleteFilePath);
				
				if(file == null) { // 입력 파일이 없다면 기본 파일 넣어주기
					String filePath = BASE_PATH + "default.png";
					user.setProfile_thumbnail(filePath);
				} else {
					// 이미지 삽입
					String filePath = BASE_PATH + user.getId() + "-" + file.getOriginalFilename();
					File dest = new File(filePath);
					file.transferTo(dest);
					
					if(!dest.exists()) { // 파일 존재 x, 이 부분이 현재 null 값이 들어갈 수 있음, 추후 다시 해야됨
			            System.out.println("파일 업로드 실패");
			        }else {
			        	user.setProfile_thumbnail(filePath);
			        }
				}
			}

			userRepository.save(user);
			return "ok";
		} catch (Exception e) {
		
			e.printStackTrace();
		}

		return "none";
	}
	
	public void insertUserInterest(User user, String[] interestReq) { // 회원 관심사 저장
		
		for(String s : interestReq) {
			if(s.length() == 0) continue;
			s = s.replace(" ", ""); // 해시태그 공백제거
			Interest exist = interestRepository.findByInterest(s); // 해당 단어가 관심사 테이블에 있는지 확인
			if(exist == null) { // 만약 관심사가 존재안한다면
				Interest interest = new Interest(s);
				interestRepository.save(interest); // 관심사 insert
				exist = interestRepository.findByInterest(s); // 다시 찾기
			}
			
			UserInterest userInterest = new UserInterest(user, exist); // 유저관심사 테이블에 저장
			userInterestRepository.save(userInterest);
		}
	}
	
	public void deleteUserInterest(List<Long> interestIds, User user) {
		
		if(interestIds != null) { // 현재 관심사가 있을 때 user_interest 테이블에서 내 정보 지우기
			for(Long id : interestIds) {
				System.out.println("user_id = " + user.getId() + " interest_id = " + id);
				userInterestRepository.deleteUserInterest(user.getId(),id);
			}
		}
	}

	


	@Override
	public void likeStudy(User user, Study study) {
		userLikeStudyRepository.save(new UserLikeStudy(user, study));
	}

	@Override
	public void unlikeStudy(User user, Study study) {
		userLikeStudyRepository.deleteByUserIdAndStudyId(user.getId(), study.getId());
	}

	@Override
	public Optional<User> getUserByUserNickname(String name) {
		return userRepository.findByName(name);
	}
	public User postSocialLoginInfo(String email) {
		
		User user = new User();
		user.setEmail(email);
		
		// @앞까지 자르기
		String user_id = email.substring(0, email.indexOf("@"));
		int user_idLen = user_id.length();

		// 아이디 중복 체크
		while(true) {
			// 아이디 중복 체크
			User exist = getUserByUserId(user_id);
			
			if(exist == null) { // 아이디 중복이 안되면
				user.setUserId(user_id);
				break;
			}
			
			// 중복된다면 원래 문자(@앞까지) + 6자리 숫자
			user_id = user_id.substring(0, user_idLen) + EmailService.createKey();
		}

		// 닉네임 중복 체크(true여야 사용가능)
		while(true) {
			if(checkName(user_id)) { // 닉네임 중복 안되면
				user.setName(user_id);
				break;
			}
			
			// 중복된다면 원래 문자(@앞까지) + 6자리 숫자
			user_id = user_id.substring(0, user_idLen) + EmailService.createKey();
		}
		
		user.setPassword(user_id); // 비밀번호는 닉네임 값으로
		user.setPlatform_type(email.substring(email.indexOf("@")+1, email.indexOf("."))); // 플랫폼 타입
		user.setAccess_token("none"); // 어쎄스 토큰은 없음
		user.setTemperature(36.5f); // 기본 온도
		user.setPosition("user"); // 회원 등급?
		
		
		//프로필 이미지 저장 (기본 이미지) 
		String filePath = BASE_PATH + "default.png";
		user.setProfile_thumbnail(filePath);
		
		userRepository.save(user); // DB 저장
		return user;
	}

	@Override
	public User getUserByEmail(String email) { // 맨 위에 하나만 리턴하기
		return userRepository.findTop1ByEmail(email);
	}

	@Override
	public User getUserByName(String name) {
		Optional<User> user = userRepository.findByName(name);
		
		if(!user.isPresent())
			return null;
		return user.get();
	}

}
