package com.ppakgom.api.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		return studyApplyRepository.findBySenderId(userId);
	}

	@Override
//	studyId로 해당 스터디 방장이 초대한 목록을 가져온다.
	public List<StudyApply> getInviteListByStudy(Long studyId) {
		
		return studyApplyRepository.findByStudyId(studyId);
	}

}
