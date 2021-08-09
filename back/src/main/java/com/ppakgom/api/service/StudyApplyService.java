package com.ppakgom.api.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.InviteReq_receiver;
import com.ppakgom.api.request.InviteReq_sender;
import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.api.request.StudyRatePostReq;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyApply;
import com.ppakgom.db.entity.User;

public interface StudyApplyService {

	void inviteStudy(User sender, Study study, User receiver, boolean is_join);

	List<StudyApply> getInviteList(Long userId);

	List<StudyApply> getInviteListByStudy(Long studyId);

	List<StudyApply> getInvitedList(Long userId, boolean isJoin);


	void cancelInvitation(InviteReq_receiver req, Long userId);

	void rejectInvitation(InviteReq_sender req, Long userId);

	void confirmRejectedInvitation(InviteReq_receiver req, Long userId);

	void agreeInvitation(InviteReq_sender req, Long userId);
	
}
