package com.ppakgom.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ConferenceCategory extends BaseEntity{
	String name;

	@Override
    public String toString() {
    	return this.id+", "+this.name;
    }
//    @OneToMany(mappedBy = "conferenceCategory")
//    List<Conference> conferenceList = new ArrayList<>();

}
