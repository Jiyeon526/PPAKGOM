package com.ppakgom.api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.ConferenceCategoryRegisterPostReq;
import com.ppakgom.api.request.ConferenceModifyReq;
import com.ppakgom.api.request.ConferenceRegisterPostReq;
import com.ppakgom.api.response.ConferenceListRes;
import com.ppakgom.db.entity.Conference;
import com.ppakgom.db.entity.ConferenceCategory;
import com.ppakgom.db.entity.User;

public interface ConferenceService {
	
	void createConferenceCategory(ConferenceCategoryRegisterPostReq conferenceCategoryRegisterPostReq);
	List<ConferenceCategory> getConferenceCategories();
	Conference createConference(ConferenceRegisterPostReq registerInfo, User user, MultipartFile thumbnail) throws IllegalStateException, IOException;
	ConferenceListRes getConferenceListRes(String title, String titleSort, String timeSort, int page, int size,Long conference_category);
	Conference getConferenceInfo(Long conference_id);
	String getCategory(Long id);
	boolean modifyConferenceInfo(Conference conference, ConferenceModifyReq req, MultipartFile file);

}
