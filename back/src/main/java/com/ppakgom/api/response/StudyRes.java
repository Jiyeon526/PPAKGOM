package com.ppakgom.api.response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.UserStudy;
import com.ppakgom.db.repository.InterestRepository;
import com.ppakgom.db.repository.StudyInterestRepository;
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
	
	@Autowired
	StudyInterestRepository studyInterestRepository;

	@Autowired
	InterestRepository interestRepository;

	@Autowired
	UserStudyRepository userStudyRepository;

	public StudyRes of(Study study) {

		StudyRes res = new StudyRes();
		System.out.println("@@@@@@@@@@@@@@@@@@@"+study);
		
//		1. study-interest 테이블에서 interest id 가져온다.
		System.out.println("############# "+study.getId());
		
//		????????!!!!!!!!!!!
		List<Object> ABCD = studyInterestRepository.findByStudyId(study.getId());
		
//		2. interest 배열에 interst id를 가지고 그 name을 가져오고, interest 배열 속성에 추가시킨답.
		res.setInterest(new ArrayList<>());
		
		for (Object id : ABCD) {
			System.out.println("아뒤ㅣㅣ "+id);
			Long iId1 = (Long)id;
			res.getInterest().add(interestRepository.findById(iId1).get().getName());
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
