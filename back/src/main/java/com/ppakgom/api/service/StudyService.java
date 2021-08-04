package com.ppakgom.api.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;

public interface StudyService {
	Study createStudy(StudyCreatePostReq studyInfo, User user, MultipartFile studyThumbnail) throws IllegalStateException, IOException;
}
