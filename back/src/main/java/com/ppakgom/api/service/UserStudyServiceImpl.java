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
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserInterest;
import com.ppakgom.db.entity.UserStudy;
import com.ppakgom.db.repository.InterestRepository;
import com.ppakgom.db.repository.UserInterestRepository;

import com.ppakgom.db.repository.UserRepository;
import com.ppakgom.db.repository.UserStudyRepository;

@Service("UserStudyService")
public class UserStudyServiceImpl implements UserStudyService {

	@Autowired
	UserStudyRepository userStudyRepository;
	
	@Override
//	해당 스터디에 가입한 인원을 가져온다.
	public List<UserStudy> getCurrentMember(Long studyId) {
		return userStudyRepository.findByStudyId(studyId);
	}

	@Override
	public void addMember(Study study, User newUser) {
		userStudyRepository.save(new UserStudy(newUser, study));
	}

	@Override
	public List<UserStudy> findUserStudyByUserId(Long userId) {
		return userStudyRepository.findByUserId(userId);
	}


}
