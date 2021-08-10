package com.ppakgom.api.service;

import java.util.List;

import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserStudy;
import com.ppakgom.db.entity.StudyRate;

public interface StudyRateService {

	void addRating(Study study, List<UserStudy> userStudy, User newUser);
	List<StudyRate> getRateListByUserId(Long userId);

}
