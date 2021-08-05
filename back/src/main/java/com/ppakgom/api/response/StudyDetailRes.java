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

	Long owner_id;

	public StudyDetailRes of2(Study study, StudyInterestRepository studyInterestRepository, UserStudyRepository userStudyRepository) {
		super.of(study, studyInterestRepository, userStudyRepository);
		this.setOwner_id(study.getUser().getId());
		return this;
	}

}
