package com.ppakgom.api.response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyInterest;
import com.ppakgom.db.entity.UserInterest;
import com.ppakgom.db.entity.UserStudy;
import com.ppakgom.db.repository.InterestRepository;
import com.ppakgom.db.repository.StudyInterestRepository;
import com.ppakgom.db.repository.StudyRepository;
import com.ppakgom.db.repository.UserStudyRepository;

import lombok.Getter;
import lombok.Setter;

//Study 객체를 응답 객체에 맞게 후처리 해주는 클래스
@Setter
@Getter
public class StudyRes {

	ArrayList<String> interest;
	String name;
	String content;
	int population;
	String study_thumbnail;
	int joined_population; // 가입한 회원 수
//	String deadline;// 마감날짜
	

	public StudyRes of(Study study, StudyInterestRepository studyInterestRepository, UserStudyRepository userStudyRepository) {

		StudyRes res = new StudyRes();
		
//		1. study-interest 테이블에서 우리 스터디에 맞는 관심사들 가져옴.
		List<StudyInterest> studyInterests = studyInterestRepository.findByStudyId(study.getId());
		
//		2. studyInterest 객체에서 interest를 가져온 뒤, 그 name을 가져오고, interest 배열 속성에 추가시킨답.
		res.setInterest(new ArrayList<>());
//		
		for (StudyInterest si : studyInterests) {
			System.out.println(si);
			res.getInterest().add(si.getInterest().getName());
		}

//		3. name, content, population, study_thumbnail은 그대로 가져오기
		res.setName(study.getName());
		if (study.getContent() != null)
			res.setContent(study.getContent());
		res.setPopulation(study.getPopulation());
		if (study.getStudy_thumbnail() != null)
			res.setStudy_thumbnail(study.getStudy_thumbnail());

//		5. studyId를 가지고 user-study 에서 user의 수를 가져온다. -> joined_population 값 설정.
		res.setJoined_population(userStudyRepository.getJoinedUserByStudyId(study.getId()));

		return res;
	}
}
