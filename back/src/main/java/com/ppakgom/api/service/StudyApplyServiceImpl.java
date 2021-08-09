package com.ppakgom.api.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppakgom.api.request.InviteReq_receiver;
import com.ppakgom.api.request.InviteReq_sender;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyApply;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.repository.StudyApplyRepository;
import com.ppakgom.db.repository.StudyRepository;

@Service("StudyApplyService")
public class StudyApplyServiceImpl implements StudyApplyService {

	@Autowired
	StudyApplyRepository studyApplyRepository;

	@Autowired
	StudyRepository studyRepository;
	
	//	sender가 receiver를 study에 초대한다. is_join이 false이고 디폴트 상태는 2
	@Override
	public void inviteStudy(User sender, Study study, User receiver, boolean is_join) {

		studyApplyRepository.save(new StudyApply(sender, study, receiver, is_join, (short) 2));
	}

	@Override
	public List<StudyApply> getInviteList(Long userId) {
		return studyApplyRepository.findBySenderIdAndIsJoin(userId, false);
	}

	@Override
//	studyId로 해당 스터디 방장이 초대한 목록을 가져온다.
	public List<StudyApply> getInviteListByStudy(Long studyId) {
		
		return studyApplyRepository.findByStudyId(studyId);
	}

//	내가 '받은' 초대를 보여줄 때 -> 상태가 '대기'일 때만 보여줌.
	@Override
	public List<StudyApply> getInvitedList(Long userId, boolean isJoin) {
		return studyApplyRepository.findByReceiverIdAndIsJoin(userId, isJoin);
	}

//	userId가 보낸 요청을 취소한다.
	@Override
	public void cancelInvitation(InviteReq_receiver req, Long userId) {
		
		studyApplyRepository.deleteBySenderIdAndStudyIdAndReceiverId(userId, req.getStudyId(), req.getReceiverId());
	}

	@Override
	public void rejectInvitation(InviteReq_sender req, Long userId) {
		StudyApply studyApply = studyApplyRepository.findByReceiverIdAndStudyIdAndSenderId(userId,req.getStudyId(),req.getSenderId());
//		상태를 거절(1)로 변경
		studyApply.setState((short) 1);
		studyApplyRepository.save(studyApply);
	}

	@Override
	public void confirmRejectedInvitation(InviteReq_receiver req, Long userId) {
		studyApplyRepository.deleteBySenderIdAndStudyIdAndReceiverIdAndState(userId, req.getStudyId(), req.getReceiverId(), (short) 1);
	}

//	스터디 초대승낙 -> study_apply 테이블에서 삭제.
	@Override
	public void agreeInvitation(InviteReq_sender req, Long userId) {
		studyApplyRepository.deleteBySenderIdAndStudyIdAndReceiverId(req.getSenderId(), req.getStudyId(), userId);
	}

}
