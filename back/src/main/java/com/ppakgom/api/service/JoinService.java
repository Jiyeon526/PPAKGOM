package com.ppakgom.api.service;

import java.util.List;

import com.ppakgom.api.request.JoinApplyReq;
import com.ppakgom.api.response.JoinApplyListRes;
import com.ppakgom.db.entity.StudyApply;

public interface JoinService {

	List<JoinApplyListRes> getJoinApplyList(Long user_id);
	StudyApply getStudyApply(Long userid, JoinApplyReq joinApplyCancelReq);
	void deleteJoinApply(StudyApply studyApply);
	List<JoinApplyListRes> getJoinApplyOwnerList(Long userid);

}
