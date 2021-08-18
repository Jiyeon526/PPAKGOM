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
	public List<StudyApply> getInviteListByStudyAndIsJoin(Long studyId ,boolean isJoin) {
		
		return studyApplyRepository.findByStudyIdAndIsJoin(studyId, isJoin);
	}

//	내가 '받은' 초대를 보여줄 때 -> 상태가 '대기'일 때만 보여줌.
	@Override
	public List<StudyApply> getInvitedList(Long userId, boolean isJoin) {
		return studyApplyRepository.findByReceiverIdAndIsJoin(userId, isJoin);
	}

//	userId가 보낸 요청을 취소한다.
	@Override
	public void cancelInvitation(InviteReq_receiver req, Long userId, boolean isJoin) {
		studyApplyRepository.deleteBySenderIdAndStudyIdAndReceiverIdAndIsJoin(userId, req.getStudyId(), req.getReceiverId(), isJoin);
	}

	@Override
	public void rejectInvitation(InviteReq_sender req, Long userId) {
		StudyApply studyApply = studyApplyRepository.findByReceiverIdAndStudyIdAndSenderIdAndIsJoin(userId,req.getStudyId(),req.getSenderId(), false);
//		상태를 거절(1)로 변경
		studyApply.setState((short) 1);
		studyApplyRepository.save(studyApply);
	}

	@Override
	public void confirmRejectedInvitation(InviteReq_receiver req, Long userId) {
		studyApplyRepository.deleteBySenderIdAndStudyIdAndReceiverIdAndStateAndIsJoin(userId, req.getStudyId(), req.getReceiverId(), (short) 1, false);
	}

//	스터디 초대승낙 -> study_apply 테이블에서 삭제.
	@Override
	public void agreeInvitation(InviteReq_sender req, Long userId) {
		studyApplyRepository.deleteBySenderIdAndStudyIdAndReceiverIdAndIsJoin(req.getSenderId(), req.getStudyId(), userId, false);
	}

	@Override
	public Optional<StudyApply> getInviteListByStudyAndIsJoinAndReceiverId(Long studyId, boolean b, Long receiver_id) {
		return studyApplyRepository.findByStudyIdAndIsJoinAndReceiverId(studyId, b, receiver_id);
	}

	@Override
	public Optional<StudyApply> findStudyApplyBySenderIdAndReceiverIdAndIsJoinAndStudyId(Long senderId, Long receiverId,
			boolean isJoin, Long studyId) {
		return studyApplyRepository.findStudyApplyBySenderIdAndReceiverIdAndIsJoinAndStudyId(senderId,receiverId,isJoin,studyId);
	}



}
