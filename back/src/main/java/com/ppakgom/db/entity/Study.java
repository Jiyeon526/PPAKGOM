package com.ppakgom.db.entity;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

	OffsetDateTime deadline;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	User user;
	
}
