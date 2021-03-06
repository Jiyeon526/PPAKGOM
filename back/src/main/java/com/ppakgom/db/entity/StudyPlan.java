package com.ppakgom.db.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudyPlan extends BaseEntity {
	
	String title;
	String detail;
	String color;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id")
	Study study;

	public StudyPlan(String title, Date date, Study study, String color) {
		super();
		this.title = title;
		this.date = date;
		this.study = study;
		this.color = color;
	}

	public StudyPlan() {
		super();
		// TODO Auto-generated constructor stub
	}

}
