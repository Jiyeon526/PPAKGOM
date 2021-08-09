package com.ppakgom.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyRate;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserInterest;
import com.ppakgom.db.entity.UserStudy;
import com.ppakgom.db.repository.StudyRateRepository;
import com.ppakgom.db.repository.UserInterestRepository;

@Service("StudyRateService")
public class StudyRateServiceImpl implements StudyRateService {

//	   public StudyRate(Study study, User user, User studyMember, boolean check) {

	@Autowired
	StudyRateRepository studyRateRepository;

	@Override
	public void addRating(Study study, List<UserStudy> userStudy, User newUser) {

		for (UserStudy us : userStudy) {
			User originUser = us.getUser();
			System.out.println("기존 멤버 "+originUser);
//			팀원들이 새로운 멤버를 평가해야 하고
			studyRateRepository.save(new StudyRate(study, originUser, newUser, false));
//			새로운 멤버도 팀원들을 평가해야 함.
			studyRateRepository.save(new StudyRate(study, newUser, originUser, false));
		}

	}

}
