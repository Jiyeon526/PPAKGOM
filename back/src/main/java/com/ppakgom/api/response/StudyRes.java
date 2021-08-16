package com.ppakgom.api.response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	Long study_id;
	ArrayList<String> interest;
	String name;
	String content;
	int population;
	String study_thumbnail;
	int joined_population; // 가입한 회원 수
	String deadline;// 마감날짜
	Long owner_id;
	float temperature; 
	boolean enter;
	boolean liked;
	
	public StudyRes of(Study study, StudyInterestRepository studyInterestRepository, UserStudyRepository userStudyRepository, List<Study> studyList, List<Study> likedStudyList) {

		StudyRes res = new StudyRes();
		
//		1. study-interest 테이블에서 우리 스터디에 맞는 관심사들 가져옴.
		List<StudyInterest> studyInterests = studyInterestRepository.findByStudyId(study.getId());
		
//		2. studyInterest 객체에서 interest를 가져온 뒤, 그 name을 가져오고, interest 배열 속성에 추가시킨답.
		res.setInterest(new ArrayList<>());
//		
		for (StudyInterest si : studyInterests) {
			res.getInterest().add(si.getInterest().getName());
		}

//		3. name, content, population, study_thumbnail, study_id,ower_id는 그대로 가져오기
		res.setName(study.getName());
		if (study.getContent() != null)
			res.setContent(study.getContent());
		res.setPopulation(study.getPopulation());
		if (study.getStudy_thumbnail() != null)
			res.setStudy_thumbnail(study.getStudy_thumbnail());
		res.setStudy_id(study.getId());
		res.setOwner_id(study.getUser().getId());
		
//		4. studyId를 가지고 user-study 에서 user의 수를 가져온다. -> joined_population 값 설정.
		res.setJoined_population(userStudyRepository.getJoinedUserByStudyId(study.getId()));
		
//		5.마감 날짜 파싱해서 집어넣기
		res.setDeadline(parseDate(study.getDeadline()));
		
//		6. 열정도 추가
		res.setTemperature(study.getTemperature());
		
//		입장버튼 추가
		for(Study s : studyList) {
			if(res.getStudy_id().equals(s.getId())) {
				res.setEnter(true);
				break;
			}
		}
//		찜한 스터디인지 아닌지.
		if(likedStudyList != null) {
			for(Study s : likedStudyList) {
				if(res.getStudy_id().equals(s.getId())) {
					res.setLiked(true);
					break;
				}
			}
		}
		return res;
	}

//	날짜형 (시간 포함)을 문자열로
	private String parseDate(Date deadline) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		String str = dateFormat.format(deadline);
		
		return str;
	}
}
