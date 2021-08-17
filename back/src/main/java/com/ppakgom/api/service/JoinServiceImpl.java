package com.ppakgom.api.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppakgom.api.request.JoinApplyReq;
import com.ppakgom.api.response.JoinApplyListRes;
import com.ppakgom.api.response.StudyJoinApplyListRes;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyApply;
import com.ppakgom.db.entity.StudyRate;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserStudy;
import com.ppakgom.db.repository.StudyApplyRepository;
import com.ppakgom.db.repository.StudyRateRepository;
import com.ppakgom.db.repository.StudyRepository;
import com.ppakgom.db.repository.UserRepository;
import com.ppakgom.db.repository.UserStudyRepository;

@Service("JoinService")
public class JoinServiceImpl implements JoinService {
	
	@Autowired
	StudyApplyRepository studyApplyRepository;
	
	@Autowired
	StudyRepository studyRepository;
	
	@Autowired
	UserStudyRepository userStudyRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	StudyRateRepository studyRateRepository;
	
	@Override
	public List<JoinApplyListRes> getJoinApplyList(Long user_id) { // 가입 신청 현황 가져오기
		
		List<StudyApply> studyApply = studyApplyRepository.findBySender_Id(user_id); // 가입 신청 리스트
		List<JoinApplyListRes> res = new ArrayList<>();
		
		if(studyApply == null) return null; // 가입 신청 없으면 null 반환
		
		for(StudyApply study : studyApply) {
			if(study.isJoin() == false) continue; // 초대이면 넘기기
			JoinApplyListRes list = new JoinApplyListRes();
			// 스터디 정보 가져오기
			Optional<Study> s = studyRepository.findById(study.getStudy().getId());
			if(!s.isPresent()) continue;
			list.setStudy_id(s.get().getId()); // 스터디 순번 저장
			list.setStudy_name(s.get().getName()); // 스터디 이름 저장
			list.setOwner_user_name(s.get().getUser().getName()); // 스터디 방장 닉네임 저장
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
	public void deleteJoinApply(StudyApply studyApply) { // 가입 취소하기
		studyApplyRepository.deleteById(studyApply.getId());
	}

	@Override
	public List<JoinApplyListRes> getJoinApplyOwnerList(Long userid) { // 가입 요청 현황 가져오기
		
		List<StudyApply> studyApply = studyApplyRepository.findByReceiver_Id(userid); // 가입 요청 리스트
		List<JoinApplyListRes> res = new ArrayList<>();
		
		if(studyApply == null) return null; // 가입 요청 없으면 null 반환
		
		for(StudyApply study : studyApply) {
			if(study.isJoin() == false || study.getState() != 2) continue; // 초대이거나 상태가 이미 승인/거절한거면 넘기기
			JoinApplyListRes list = new JoinApplyListRes();
			// 스터디 정보 가져오기
			Optional<Study> s = studyRepository.findById(study.getStudy().getId());
			if(!s.isPresent()) continue; // 해당 스터디가 존재하지 않으면 넘기기
			list.setStudy_id(s.get().getId()); // 스터디 순번 저장
			list.setStudy_name(s.get().getName()); // 스터디 이름 저장
			list.setOwner_user_name(s.get().getUser().getName()); // 스터디 방장 이름
			list.setUser_id(study.getSender().getId());
			list.setState(study.getState()); // 상태 저장
			res.add(list);
		}
		
		return res;
	}

	@Override
	public String studyApplyOk(StudyApply studyApply) {
		
		// 현재 스터디 인원
		int studyPopulation = userStudyRepository.getJoinedUserByStudyId(studyApply.getStudy().getId());
		// 해당 스터디 정보
		Optional<Study> study = studyRepository.findById(studyApply.getStudy().getId());
		// study_apply에서 삭제
		deleteJoinApply(studyApply);
	
		// 스터디 존재안함
		if(!study.isPresent()) return "error";
		
		// 스터디 인원 초과
		if(studyPopulation >= study.get().getPopulation())
			return "population";
		
		// 스터디 평가에 사람 추가
		// 스터디원들 가져오기
		List<UserStudy> userStudyList = userStudyRepository.findByStudyId(study.get().getId());
		for(UserStudy us : userStudyList) {
			User originUser = us.getUser();
//					팀원들이 새로운 멤버를 평가해야 하고
			studyRateRepository.save(new StudyRate(study.get(), originUser, studyApply.getSender(), false));
//					새로운 멤버도 팀원들을 평가해야 함.
			studyRateRepository.save(new StudyRate(study.get(), studyApply.getSender(), originUser, false));
		}
		// 스터디에 유저 추가
		UserStudy userStudy = new UserStudy(studyApply.getSender(), study.get());
		userStudyRepository.save(userStudy);
		
		return "ok";
	}

	@Override
	public StudyApply getStudyApplyReceiver(Long userid, JoinApplyReq joinApplyReq) {
		// null 값이 들어 올 경우  
		if(userid == null || joinApplyReq == null || 
				joinApplyReq.getUser_id() == null || joinApplyReq.getStudy_id() == null)
			return null;
		
		StudyApply studyApply = 
				studyApplyRepository.findByReceiver_IdAndStudy_IdAndSender_Id(
						userid, 
						joinApplyReq.getStudy_id(),
						joinApplyReq.getUser_id());
		
		return studyApply;
	}

	@Override
	public void modifyJoinApply(StudyApply studyApply) { // 가입 거절, 상태 1로 바꾸기
		studyApply.setState((short) 1);
		studyApplyRepository.save(studyApply);
	}

	@Override
	public List<StudyJoinApplyListRes> getStudyJoinApplyList(Long studyId) {
		
		List<StudyApply> studyApply = studyApplyRepository.findByStudy_Id(studyId); // 가입 요청 리스트
		List<StudyJoinApplyListRes> res = new ArrayList<>();
		
		if(studyApply == null) return null; // 가입 요청 없으면 null 반환
		
		for(StudyApply study : studyApply) {
			if(study.isJoin() == false || study.getState() != 2) continue; // 초대이거나 상태가 이미 승인/거절한거면 넘기기
			StudyJoinApplyListRes list = new StudyJoinApplyListRes();
			// 스터디 신청한 사람들 정보 저장
			list.setUser_id(study.getSender().getUserId()); // 아이디 저장
			list.setName(study.getSender().getName()); // 닉네임 저장
			list.setThumbnail(study.getSender().getProfile_thumbnail()); // 썸네일 저장
			list.setTemperature(study.getSender().getTemperature()); // 온도 저장
			list.setState(study.getState());
			res.add(list);
		}
		
		return res;
	}

	@Override
	public String studyApply(Long studyId, Long userId) {
		// 스터디 정보 가져오기
		Optional<Study> study = studyRepository.findById(studyId);
		if(!study.isPresent()) return "error"; // 스터디 없으면 반환
		
		// 현재 스터디 인원
		int studyPopulation = userStudyRepository.getJoinedUserByStudyId(studyId);
		// 스터디 인원 초과
		if(studyPopulation >= study.get().getPopulation())
			return "population";
		
		// 모집 날짜
		DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = new Date();
		String today = sdFormat.format(nowDate); // 오늘 날짜(연, 월)
		String studyDate = sdFormat.format(study.get().getDeadline()); // 스터디 모집 날짜
		if(today.compareTo(studyDate) > 0) return "deadline";
		
		// 사용자
		User user = userRepository.findUserById(userId);
		Short state = 2; // 대기 상태
		StudyApply studyApply = new StudyApply(true, state, study.get().getUser(), 
				user, study.get());
		studyApplyRepository.save(studyApply); // DB 저장
		return "ok";
	}

}
