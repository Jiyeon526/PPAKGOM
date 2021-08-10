package com.ppakgom.db.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudyRate extends BaseEntity{
   
   boolean checked;
   
   @ManyToOne
   @JoinColumn(name = "userId")
   User user;
   
   @ManyToOne
   @JoinColumn(name = "studyId")
   Study study;
   
   @ManyToOne
   @JoinColumn(name = "studyMemberId")
   User studyMember;
   
   
   public StudyRate(Study study, User user, User studyMember, boolean check) {
	   this.setChecked(check);
	   this.setStudy(study);
	   this.setUser(user);
	   this.setStudyMember(studyMember);
   }
   
   public StudyRate() {}
}