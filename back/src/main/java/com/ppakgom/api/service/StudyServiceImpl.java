package com.ppakgom.api.service;

import java.io.File;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.api.request.StudyRatePostReq;
import com.ppakgom.api.response.StudyMemberInfoRes;
import com.ppakgom.api.response.StudyScheduleMonthRes;

import com.ppakgom.api.request.StudyScheduleReq;
import com.ppakgom.api.response.StudyScheduleMonthRes;
import com.ppakgom.api.response.StudyScoreMember;
import com.ppakgom.api.response.StudyTestInfoRes;
import com.ppakgom.api.response.StudyTestListRes;
import com.ppakgom.api.response.StudyTestScoreRes;
import com.ppakgom.api.response.StudyTestScoreTotalRes;
import com.ppakgom.api.response.StudyTests;
import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyInterest;
import com.ppakgom.db.entity.StudyRate;
import com.ppakgom.db.entity.StudyPlan;
import com.ppakgom.db.entity.StudyScore;
import com.ppakgom.db.entity.StudyTest;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserLikeStudy;
import com.ppakgom.db.entity.UserStudy;
import com.ppakgom.db.repository.InterestRepository;
import com.ppakgom.db.repository.StudyInterestRepository;
import com.ppakgom.db.repository.StudyRateRepository;
import com.ppakgom.db.repository.StudyPlanRepository;
import com.ppakgom.db.repository.StudyRepository;
import com.ppakgom.db.repository.StudyScoreRepository;
import com.ppakgom.db.repository.StudyTestRepository;
import com.ppakgom.db.repository.UserLikeStudyRepository;
import com.ppakgom.db.repository.UserRepository;
import com.ppakgom.db.repository.UserStudyRepository;

@Service("StudyService")
public class StudyServiceImpl implements StudyService {

	@Autowired
	StudyRepository studyRepository;

	@Autowired
	InterestRepository interestRepository;

	@Autowired
	StudyInterestRepository studyInterestRepository;

	@Autowired
	UserStudyRepository userStudyRepository;
	
	@Autowired
	StudyPlanRepository studyPlanRepository;
	
	@Autowired
	StudyScoreRepository studyScoreRepository;
	
	@Autowired
	StudyTestRepository studyTestRepository;
	
	String BASE_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\image\\study\\";
	
	
	UserLikeStudyRepository userLikeStudyRepository;

	@Autowired
	StudyRateRepository studyRateRepository;
	
	@Autowired
	UserRepository userRepository;
	
	/* 스터디 생성 */
	@Override
	public Study createStudy(StudyCreatePostReq studyInfo, User user, MultipartFile studyThumbnail)
			throws IllegalStateException, IOException, ParseException {

		Study study = new Study();
		study.setName(studyInfo.getName());
		study.setContent(studyInfo.getContent());
		study.setTemperature(studyInfo.getTemperature());
		study.setPopulation(studyInfo.getPopulation());
		study.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse(studyInfo.getDeadline()));

//		방 만든 사람이 방장!
		study.setUser(user);

//		스터디 아이디 미리 뽑아두기
		Study studyTmp = studyRepository.save(study);
		Long studyId = studyTmp.getId();

//		 사진 관련 처리 -> image/study/방번호-파일명
		String path = BASE_PATH + studyId + "-" + studyThumbnail.getOriginalFilename();
		File dest = new File(path);
		studyThumbnail.transferTo(dest);
		study.setStudy_thumbnail(getShortFilePath(path));


		if (studyInfo.getInterest() != null) {
//		 관심사 테이블.
			ArrayList<Interest> interests = new ArrayList<>();
			String[] interestsName = studyInfo.getInterest();
			for (String interest : interestsName) {
//			중복 검사 실시 -> 중복한다면 그 값을 가져오고, 중복하지 않는다면 insert해서 id를 가져온다.
				Interest i;
				i = interestRepository.findByInterest(interest);
				if (i == null)
					i = interestRepository.save(new Interest(interest));
				interests.add(i);
			}

//		 관심사 - 스터디 테이블.
			for (Interest i : interests) {
				studyInterestRepository.save(new StudyInterest(i, studyTmp));
			}
		}
//		회원 - 스터디 테이블
		userStudyRepository.save(new UserStudy(user, studyTmp));

		return studyRepository.save(study);
	}

	private String getShortFilePath(String path) {
		int idx = path.indexOf("image");
		return path.substring(idx, path.length());

	}

	@Override
	public List<Study> getAllStudy() {
		return studyRepository.findAll();
	}

	@Override
	public Optional<Study> getStudyById(Long studyId) {
		return studyRepository.findById(studyId);
	}

	@Override
	public List<Study> getStudyByName(String name) {
		return studyRepository.findByName(name);
	}

	@Override
	public List<Study> getStudyByInterest(String interest) {

		List<Study> resultSet = new LinkedList<>();

//		하나의 스터디가 여러개 일 경우 대비 
//		(예: 관심사 쿼리를  '면접' 이라고 전달했는데 스터디의 관심사가 '삼성면접', 'LG면접' 이면 하나의 스터디가 여러 개 들어감)
		HashSet<Study> temp = new HashSet<>();

//		1. 관심사들 검색 -> like 쿼리 (복수)
		List<Interest> interests = interestRepository.findAllByName(interest);

//		2. 관심사가 있는 경우 -> 스터디 - 관심사 테이블에서 해당 관심사를 가지고 있는 스터디 가져오기

		for (Interest i : interests) {
//			스터디 아이디 가져오고
			List<Long> studyIdWithInterest = studyInterestRepository.findByInterestId(i.getId());

//			스터디 테이블에서 스터디 아이디로 스터디 자체를 가져오기.
			for (Long sId : studyIdWithInterest) {
				Optional<Study> s = studyRepository.findById(sId);
				if (s.isPresent()) {
					temp.add(s.get());
					if (temp.size() == 3) {
//						3. 집합을 리스트로(반환 위해서) -> 3개 초과할 경우.
						for (Study study : temp) {
							resultSet.add(study);
						}
						return resultSet;
					}
				}
			}
		}
//		3. 집합을 리스트로(반환 위해서)
		for (Study s : temp) {
			resultSet.add(s);
		}

		return resultSet;
	}

	@Override
	public List<Study> getUserLikeStudy(User user) {
		
		List<UserLikeStudy> tmp = new ArrayList<>();
		List<Study> resultSet = new ArrayList<>();
		
		tmp = userLikeStudyRepository.findByUserId(user.getId());
		for(UserLikeStudy uls : tmp) {
			resultSet.add(uls.getStudy());
		}
		
		return resultSet;
	}

	@Override
	public List<Study> getUserJoinStudy(User user) {
		
		List<UserStudy> tmp = new ArrayList<>();
		List<Study> resultSet = new ArrayList<>();
		
		tmp = userStudyRepository.findByUserId(user.getId());
		
		for(UserStudy us : tmp) {
			resultSet.add(us.getStudy());
		}
		
		return resultSet;
	}

	@Override
	public void rateStudy(User user, StudyRatePostReq rateInfo) {
		
//		스터디 ID로 스터디 찾고
		Study study = studyRepository.findById(rateInfo.getStudyId()).get();
//		멤버 ID로 멤버 찾기
		User member = userRepository.findUserById(rateInfo.getStudyMemberId());
//		평가 점수로 열정도 업데이트 로직
		updateTemperature(member, rateInfo.getRating());
		
//		평가 다 했다고 check
		StudyRate studyRating = 
				studyRateRepository.findByUserIdAndStudyIdAndStudyMemberId(user.getId(), study.getId(), member.getId());
		studyRating.setChecked(true);
		
		studyRateRepository.save(studyRating);
	}
	
	//member의 열정도를 업데이트 하좌.
//	0개 = -1.8 / 1개 = - 0.8 / 2개 = -0.2 / 3개 = 0 / 4개 = +0.2 / 5개 = +0.8
//	( (abs(별 갯수 - 3) * (별갯수 - 3) ) / 5) ? + 기존 온도에 더하기
	public void updateTemperature(User member, int rate) {
//		현재 열정도 가져오기
		float curTemperature = member.getTemperature();
		member.setTemperature(Math.abs(rate - 3f) * (rate - 3f) / 5 + curTemperature);
	}
		
	public List<StudyScheduleMonthRes> getStudyScheduleMonth(Long studyId, int month) {
		
		// 해당 스터디 일정 전부 가져오기
		List<StudyPlan> list = studyPlanRepository.findByStudy_Id(studyId);
		List<StudyScheduleMonthRes> res = new ArrayList<>();
		
		for(StudyPlan studyPlan : list) {
			DateFormat sdFormat = new SimpleDateFormat("yyyy-MM");
			Date nowDate = new Date();
			String today = sdFormat.format(nowDate); // 오늘 날짜(연, 월)
			
			String studyMonth = Integer.toString(month); // 검색할 월
			if(studyMonth.length() != 2) studyMonth = "0" + studyMonth;
			today = today.substring(0, 5) + studyMonth; 
			
			String studySchedule = sdFormat.format(studyPlan.getDate()); // 스터디 날짜
			
			if(!today.equals(studySchedule)) continue; // 날짜 다르면 넘김
			
			StudyScheduleMonthRes s = new StudyScheduleMonthRes(studyPlan.getId(), 
					studyPlan.getTitle(), studyPlan.getDetail(), studyPlan.getDate());
			// 저장
			res.add(s);
		}
		
		return res;
	}

	@Override
	public boolean postStudySchedule(Long studyId, StudyScheduleReq req) {
		
		try {
			// 해당 스터디 가져오기
			Optional<Study> study = studyRepository.findById(studyId);
			// 없다면 false 리턴
			if(!study.isPresent()) return false;
			
			// 날짜 변환
			Date date;
			date = new SimpleDateFormat("yyyy-MM-dd").parse(req.getDate());
			
			// 스터디 일정 객체 생성
			StudyPlan studyPlan = new StudyPlan(req.getTitle(), 
					req.getDetail(), date,study.get());
			// 객체 생성 안되면 false
			if(studyPlan == null) return false;
			
			// DB에 저장
			studyPlanRepository.save(studyPlan);
			
		} catch (ParseException e) {
			System.out.println("날짜 변환 에러");
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public List<StudyTestScoreTotalRes> getStudyTestScore(Long studyId) {
		
		if(studyId == null) return null;
		
		// 해당 스터디의 스터디원들 가져오기
		List<UserStudy> studyUsersIds = userStudyRepository.findByStudyId(studyId);

		// 해당 스터디에 있는 스터디 문제집들 가져오기
		List<StudyTest> studyTests = studyTestRepository.findByStudy_Id(studyId);
		
		if(studyTests == null || studyUsersIds == null) return null; // 문제집이나 회원이 아예 없을 때 
		
		// 스터디 문제집에 따른 멤버들 점수 저장
		List<StudyTestScoreTotalRes> res = new ArrayList<>();
		for(UserStudy user : studyUsersIds) {
			
			StudyTestScoreTotalRes study = new StudyTestScoreTotalRes(); // res 저장 객체
			List<StudyScoreMember> scores = new ArrayList<>(); // 문제집 별 점수
			
			study.setName(user.getUser().getName()); // 닉네임 저장

			for(StudyTest test : studyTests) {
				StudyScoreMember member = new StudyScoreMember(); // 문제집 + 해당 점수
				member.setTest_title(test.getTitle()); // 문제집 이름
				
				// 해당 문제집에서의 내 점수 
				Optional<StudyScore> score = studyScoreRepository.findByUserIdAndStudyTestId(user.getUser().getId(), test.getId());

				if(score.isPresent()) // 내 점수가 있다면
					member.setScore(score.get().getScore());
				else
					member.setScore((short) 0); // 없다면 0으로
				
				scores.add(member); // scores에 저장
			}
			
			study.setData(scores); // study 객체에 저장
			res.add(study); // 반환 객체에 저장
		}
		
		return res;
	}

	@Override
	public List<StudyTestListRes> getStudyTestList(Long studyId) {
		
		// 스터디 문제집 가져오기
		List<StudyTest> studyTest = studyTestRepository.findByStudy_Id(studyId);
		List<StudyTestListRes> res = new ArrayList<>();
		
		if(studyTest == null) return null;
		
		for(StudyTest st: studyTest) {
			StudyTestListRes s = new StudyTestListRes(st.getId(), st.getUser().getName(), st.getTitle());
			res.add(s);
		}
		
		return res;
	}

	@Override
	public List<StudyMemberInfoRes> getStudyMemberInfo(Long studyId) {
		List<UserStudy> userStudy = userStudyRepository.findByStudyId(studyId);
		if(userStudy == null) return null;
		
		List<StudyMemberInfoRes> res = new ArrayList<>();
		for(UserStudy us: userStudy) {
			StudyMemberInfoRes member = new StudyMemberInfoRes(us.getUser().getName(), us.getUser().getUserId(),
					us.getUser().getId(), us.getUser().getProfile_thumbnail());
			res.add(member);
		}
		
		return res;
	}

	@Override
	public StudyTestScoreRes postStudyTestScore(List<String> answer, Long userId, Long testId) {
		StudyTestScoreRes res = new StudyTestScoreRes();
		StudyScore score = new StudyScore();
		
		// 답 가져오기
		Optional<StudyTest> test = studyTestRepository.findById(testId);
		if(!test.isPresent()) return null;
		
		// 테이블 insert할 객체들
		score.setStudy(test.get().getStudy()); // 해당 스터디 저장
		score.setStudyTest(test.get()); // 문제집 저장
		Optional<User> user = userRepository.findById(userId); // 문제 푼 사람 저장
		if(user.isPresent()) score.setUser(user.get());
		
		res.setNumber(test.get().getNumber()); // 문항 개수 세팅
		String[] testAnswer = test.get().getAnswer().split(","); // 정답 , 로 구분하기
		
		Short cCnt = 0; // 맞은 갯수
		for(int i=0;i<testAnswer.length;i++) {
			if(answer.get(i) == null) continue;
			if(testAnswer[i].equals(answer.get(i))) {
				cCnt++;
			}
		}
		
		res.setCorrect(cCnt);
		score.setScore(cCnt);
		
		StudyScore origin = studyScoreRepository.findByStudyTestIdAndUserId(testId, userId);
		if(origin != null) {// 원래 점수가 있던 사람
			origin.setScore(cCnt);
			studyScoreRepository.save(origin);
		} else
			studyScoreRepository.save(score);
		
		return res;
	}

	@Override
	public StudyTestInfoRes getStudyTestInfo(Long studyId, Long testId) {
		StudyTestInfoRes res = new StudyTestInfoRes();
		
		// 문제집 정보
		Optional<StudyTest> st = studyTestRepository.findById(testId);
		if(!st.isPresent()) return null;
		
		// 정보 저장
		StudyTests test = new StudyTests(testId, st.get().getUser().getUserId(),
				st.get().getTitle(), st.get().getTest_url());
		// 반환 객체에 저장
		res.setTest(test);
		
		// 답 ,로 스플릿해서 저장
		String[] answer = st.get().getAnswer().split(",");
		res.setAnswer(answer);

		return res;
	}

}
