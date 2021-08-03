package com.ppakgom.db.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserConference extends BaseEntity{

//    int userId;
//    int conferenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id")
    Conference conference;
    
//    public static UserConference of (Conference conference, User user) {
//    	UserConference userConference = new UserConference();
//    	userConference.setConference(conference);
//    	userConference.setUser(user);
//    	return userConference;
//    }
}
