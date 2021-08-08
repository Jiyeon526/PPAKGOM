package com.ppakgom.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppakgom.api.request.JoinApplyReq;
import com.ppakgom.api.response.JoinApplyListRes;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyApply;
import com.ppakgom.db.repository.StudyApplyRepository;
import com.ppakgom.db.repository.StudyRepository;

@Service("JoinService")
public class JoinServiceImpl implements JoinService {
	
	@Autowired
	StudyApplyRepository studyApplyRepository;
	
	@Autowired
	StudyRepository studyRepository;
	
	@Override
	public List<JoinApplyListRes> getJoinApplyList(Long user_id) { // 가입 요청 현황 가져오기
		
		List<StudyApply> studyApply = studyApplyRepository.findBySender_Id(user_id); // 가입 요청 리스트
		List<JoinApplyListRes> res = new ArrayList<>();
		
		if(studyApply == null) return null; // 가입 요청 없으면 null 반환
		
		for(StudyApply study : studyApply) {
			if(study.is_join() == false) continue; // 초대이면 넘기기
			JoinApplyListRes list = new JoinApplyListRes();
			// 스터디 정보 가져오기
			Optional<Study> s = studyRepository.findById(study.getStudy().getId());
			if(!s.isPresent()) continue;
			list.setStudy_id(s.get().getId()); // 스터디 순번 저장
			list.setStudy_name(s.get().getName()); // 스터디 이름 저장
			list.setOwner_name(s.get().getUser().getName()); // 스터디 방장 닉네임 저장
			list.setState(study.getState()); // 상태 저장
			res.add(list);
		}
		
		return res;
	}

	@Override
	public StudyApply getStudyApply(Long userid, JoinApplyReq joinApplyCancelReq) { // 해당 study_apply 튜플 찾아오기
		
		// null 값이 들어 올 경우  
		if(userid == null || joinApplyCancelReq == null || 
				joinApplyCancelReq.getUser_id() == null || joinApplyCancelReq.getStudy_id() == null)
			return null;
		
		StudyApply studyApply = 
				studyApplyRepository.findBySender_IdAndStudy_IdAndReceiver_Id(
						userid, 
						joinApplyCancelReq.getStudy_id(),
						joinApplyCancelReq.getUser_id());
		
		return studyApply;
	}

	@Override
	public void deleteJoinApplyCancel(StudyApply studyApply) { // 가입 취소하기
		studyApplyRepository.deleteById(studyApply.getId());
	}

}
