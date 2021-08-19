package com.ppakgom.api.service;

import java.io.IOException;


import java.text.ParseException;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.api.request.StudyRatePostReq;
import com.ppakgom.api.response.AttendGetRes;
import com.ppakgom.api.response.StudyDetailInfo;
import com.ppakgom.api.response.StudyMemberInfoRes;
import com.ppakgom.api.response.StudyScheduleMonthRes;
import com.ppakgom.api.response.StudyTestInfoRes;
import com.ppakgom.api.response.StudyTestListRes;
import com.ppakgom.api.response.StudyTestScoreRes;
import com.ppakgom.api.request.StudyScheduleReq;
import com.ppakgom.api.response.StudyTestScoreTotalRes;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyPlan;
import com.ppakgom.db.entity.User;

public interface StudyService {
	Study createStudy(StudyCreatePostReq studyInfo, User user, MultipartFile studyThumbnail) throws IllegalStateException, IOException, ParseException;

	List<Study> getAllStudy();

	Optional<Study> getStudyById(Long studyId);

	List<Study> getStudyByName(String name);

	List<Study> getStudyByInterest(String interest);


	List<StudyScheduleMonthRes> getStudyScheduleMonth(Long studyId, int month);

	List<Study> getUserLikeStudy(User user);

	List<Study> getUserJoinStudy(User user);


	void rateStudy(User user, StudyRatePostReq rateInfo);

	boolean postStudySchedule(Long studyId, StudyScheduleReq req);

	List<StudyTestScoreTotalRes> getStudyTestScore(Long studyId);

	List<StudyTestListRes> getStudyTestList(Long studyId);

	List<StudyMemberInfoRes> getStudyMemberInfo(Long studyId);

	StudyTestScoreRes postStudyTestScore(List<String> answer, Long studyId, Long testId);

	StudyTestInfoRes getStudyTestInfo(Long studyId, Long testId);

	void updateStudy(Study study, MultipartFile studyThumbnail, StudyCreatePostReq studyInfo) throws ParseException, IllegalStateException, IOException;

	List<StudyPlan> getPlansByStudy(Long id);

	List<AttendGetRes> getAttendList(List<StudyPlan> studyPlans, List<User> members);
	List<StudyDetailInfo> getStudyDetailInfo(Long studyId);

	String postStudyAttend(Long studyId, Long userId);

}
