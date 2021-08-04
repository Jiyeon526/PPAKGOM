package com.ppakgom.db.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User_Interest extends BaseEntity{
	
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	User user;
	
	@JoinColumn(name = "interest_id")
	@ManyToOne(fetch = FetchType.LAZY)
	Interest interest;

}
