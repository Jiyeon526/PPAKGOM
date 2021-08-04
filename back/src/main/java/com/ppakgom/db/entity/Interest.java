package com.ppakgom.db.entity;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Interest extends BaseEntity{

	String name;
	
	public Interest(String n) {
		this.name = n;
	}
	
}
