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
public class Study extends BaseEntity{
	
	String name;
	String content;
	float temperature;
	int population;
	String study_thumbnail;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date deadline;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	User user;

	@Override
	public String toString() {
		return "Study [name=" + name + ", content=" + content + ", temperature=" + temperature + ", population="
				+ population + ", study_thumbnail=" + study_thumbnail + ", deadline=" + deadline + ", user=" + user
				+ "ID = "+id+"]";
	}

	
}
