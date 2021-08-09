package com.ppakgom.api.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.api.response.StudyScheduleMonthRes;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;

public interface StudyService {
	Study createStudy(StudyCreatePostReq studyInfo, User user, MultipartFile studyThumbnail) throws IllegalStateException, IOException, ParseException;

	List<Study> getAllStudy();

	Optional<Study> getStudyById(Long studyId);

	List<Study> getStudyByName(String name);

	List<Study> getStudyByInterest(String interest);

	List<StudyScheduleMonthRes> getStudyScheduleMonth(Long studyId, int month);
}
