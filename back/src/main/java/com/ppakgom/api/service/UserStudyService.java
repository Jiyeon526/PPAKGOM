package com.ppakgom.api.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.api.request.StudyRatePostReq;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserStudy;

public interface UserStudyService {

	List<UserStudy> getCurrentMember(Long id);

	void addMember(Study study, User newUser);
}
