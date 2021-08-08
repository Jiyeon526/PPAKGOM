package com.ppakgom.db.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * study_rate 테이블은 복합키가 PK가 된디 평가하는 회원 ID - 스터디 ID - 평가 받는 회원 ID 이 세개가 묶여서 PK가
 * 됨.
 */
@Setter
@Getter
public class StudyRateId implements Serializable {

	Long study;
	Long studyMember;
	Long user;

	public StudyRateId() {}
	
	public StudyRateId(Long s, Long u,Long sm) {
		this.setStudy(s);
		this.setStudyMember(sm);
		this.setUser(u);
	}
	
}