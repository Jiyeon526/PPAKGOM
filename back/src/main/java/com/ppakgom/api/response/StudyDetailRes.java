package com.ppakgom.api.response;

import com.ppakgom.db.entity.Study;
import com.ppakgom.db.repository.StudyInterestRepository;
import com.ppakgom.db.repository.UserStudyRepository;

import lombok.Getter;
import lombok.Setter;

//Study 객체를 응답 객체에 맞게 후처리 해주는 클래스
@Setter
@Getter
public class StudyDetailRes extends StudyRes{
	
//	이거 하나 넣자고 만든건데 사실 넘 비효율적이라서 그냥 얘기 할걸 그랬다..
	Long owner_id;

	public StudyDetailRes of2(Study study, StudyInterestRepository studyInterestRepository, UserStudyRepository userStudyRepository) {
		StudyRes res = this.of(study, studyInterestRepository, userStudyRepository);
		this.setStudy_id(res.getStudy_id());
		this.setContent(res.getContent());
		this.setDeadline(res.getDeadline());
		this.setInterest(res.getInterest());
		this.setJoined_population(res.getJoined_population());
		this.setName(res.getName());
		this.setPopulation(res.getPopulation());
		this.setStudy_thumbnail(res.getStudy_thumbnail());
		this.setOwner_id(study.getUser().getId());
		return this;
	}

}
