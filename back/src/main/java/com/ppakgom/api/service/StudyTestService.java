package com.ppakgom.api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.WorkbookCreatePostReq;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;

public interface StudyTestService {

	void createWorkbook(Study study, User writer, WorkbookCreatePostReq workbookInfo, MultipartFile testFile) throws IllegalStateException, IOException;

}
