package com.ppakgom.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 유저 모델 정의.
 */
@Entity
@Getter
@Setter
public class User extends BaseEntity{
    String position;
    String department;
    String name;
    String userId;

    @OneToMany(mappedBy = "user")
    List<UserConference> userConferenceList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<ConferenceHistory> conferenceHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Conference> conferenceList = new ArrayList<>();

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;
}
