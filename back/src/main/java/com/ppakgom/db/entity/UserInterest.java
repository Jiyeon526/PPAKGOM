package com.ppakgom.db.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
public class UserInterest extends BaseEntity {

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_id")
	Interest interest;

	public UserInterest(User user, Interest interest) {
		super();
		this.user = user;
		this.interest = interest;
	}

	public UserInterest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
