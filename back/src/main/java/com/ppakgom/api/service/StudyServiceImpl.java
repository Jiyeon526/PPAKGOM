package com.ppakgom.api.service;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.api.request.StudyRatePostReq;
import com.ppakgom.api.response.AttendGetRes;
import com.ppakgom.api.response.AttendRes;
import com.ppakgom.api.response.MemberAttend;
import com.ppakgom.api.response.StudyDetailInfo;
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
import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyAttend;
import com.ppakgom.db.entity.StudyInterest;
import com.ppakgom.db.entity.StudyRate;
import com.ppakgom.db.entity.StudyPlan;
import com.ppakgom.db.entity.StudyScore;
import com.ppakgom.db.entity.StudyTest;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserLikeStudy;
import com.ppakgom.db.entity.UserStudy;
import com.ppakgom.db.repository.InterestRepository;
import com.ppakgom.db.repository.StudyAttendRepository;
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
	
	@Autowired
	StudyAttendRepository studyAttendRepository; 

	String BASE_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\image\\";

	@Autowired
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
		String path = BASE_PATH;
		if(studyThumbnail == null) {
//			디폴트 사진 처리
			path += "default.png";
		}
		else {
			path +="study\\"+studyId + "-" + studyThumbnail.getOriginalFilename();
			File dest = new File(path);
			studyThumbnail.transferTo(dest);
		}
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

//		System.out.println("사용자의 관심사 "+interest);
		List<Study> resultSet = new LinkedList<>();

//		하나의 스터디가 여러개 일 경우 대비 
//		(예: 관심사 쿼리를  '면접' 이라고 전달했는데 스터디의 관심사가 '삼성면접', 'LG면접' 이면 하나의 스터디가 여러 개 들어감)
		HashSet<Study> temp = new HashSet<>();

//		1. 관심사들 검색 -> like 쿼리 (복수)
		List<Interest> interests = interestRepository.findAllByName(interest);

//		2. 관심사가 있는 경우 -> 스터디 - 관심사 테이블에서 해당 관심사를 가지고 있는 스터디 가져오기

		for (Interest i : interests) {
//			스터디 아이디 가져오고
//			System.out.println("가져온 관심사 "+i);
			List<Long> studyIdWithInterest = studyInterestRepository.findByInterestId(i.getId());

//			스터디 테이블에서 스터디 아이디로 스터디 자체를 가져오기.
			for (Long sId : studyIdWithInterest) {
				Optional<Study> s = studyRepository.findById(sId);
				if (s.isPresent()) {
					temp.add(s.get());
//					if (temp.size() == 3) {
//						3. 집합을 리스트로(반환 위해서) -> 3개 초과할 경우.
//						for (Study study : temp) {
//							resultSet.add(study);
//						}
//						return resultSet;
//					}
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
		for (UserLikeStudy uls : tmp) {
			resultSet.add(uls.getStudy());
		}

		return resultSet;
	}

	@Override
	public List<Study> getUserJoinStudy(User user) {

		List<UserStudy> tmp = new ArrayList<>();
		List<Study> resultSet = new ArrayList<>();

		tmp = userStudyRepository.findByUserId(user.getId());

		for (UserStudy us : tmp) {
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
		StudyRate studyRating = studyRateRepository.findByUserIdAndStudyIdAndStudyMemberId(user.getId(), study.getId(),
				member.getId());
		studyRating.setChecked(true);

		studyRateRepository.save(studyRating);
	}

	// member의 열정도를 업데이트 하좌.
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

		for (StudyPlan studyPlan : list) {
			DateFormat sdFormat = new SimpleDateFormat("yyyy-MM");
			Date nowDate = new Date();
			String today = sdFormat.format(nowDate); // 오늘 날짜(연, 월)

			String studyMonth = Integer.toString(month); // 검색할 월
			if (studyMonth.length() != 2)
				studyMonth = "0" + studyMonth;
			today = today.substring(0, 5) + studyMonth;

			String studySchedule = sdFormat.format(studyPlan.getDate()); // 스터디 날짜

			if (!today.equals(studySchedule))
				continue; // 날짜 다르면 넘김

			StudyScheduleMonthRes s = new StudyScheduleMonthRes(studyPlan.getId(), 
					studyPlan.getTitle(), studyPlan.getDate(), studyPlan.getColor());
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
			if (!study.isPresent())
				return false;

			// 날짜 변환
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(req.getDate());
			
			// 스터디 일정 객체 생성
			StudyPlan studyPlan = new StudyPlan(req.getTitle(), date, study.get(), req.getColor());
			// 객체 생성 안되면 false
			if (studyPlan == null)
				return false;

			// DB에 저장
			studyPlan = studyPlanRepository.save(studyPlan);
			
			// 스터디 출석현황 저장
			// 스터디 유저들 가져오기
			List<UserStudy> users = userStudyRepository.findByStudyId(studyId);
			for(UserStudy user: users) { // 멤버들 마다 일정 생길 때 마다 스터디 출석 현황에 넣어주기
				StudyAttend studyAttend = new StudyAttend(study.get(), user.getUser(), studyPlan, false);
				studyAttendRepository.save(studyAttend);
			}
			
		} catch (ParseException e) {
			System.out.println("날짜 변환 에러");
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public List<StudyTestScoreTotalRes> getStudyTestScore(Long studyId) {

		if (studyId == null)
			return null;

		// 해당 스터디의 스터디원들 가져오기
		List<UserStudy> studyUsersIds = userStudyRepository.findByStudyId(studyId);

		// 해당 스터디에 있는 스터디 문제집들 가져오기
		List<StudyTest> studyTests = studyTestRepository.findByStudy_Id(studyId);

		if (studyTests == null || studyUsersIds == null)
			return null; // 문제집이나 회원이 아예 없을 때

		// 스터디 문제집에 따른 멤버들 점수 저장
		List<StudyTestScoreTotalRes> res = new ArrayList<>();
		for (UserStudy user : studyUsersIds) {

			StudyTestScoreTotalRes study = new StudyTestScoreTotalRes(); // res 저장 객체
			List<StudyScoreMember> scores = new ArrayList<>(); // 문제집 별 점수

			study.setName(user.getUser().getName()); // 닉네임 저장

			for (StudyTest test : studyTests) {
				StudyScoreMember member = new StudyScoreMember(); // 문제집 + 해당 점수
				member.setTest_title(test.getTitle()); // 문제집 이름

				// 해당 문제집에서의 내 점수
				Optional<StudyScore> score = studyScoreRepository.findByUserIdAndStudyTestId(user.getUser().getId(),
						test.getId());

				if (score.isPresent()) // 내 점수가 있다면
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

		if (studyTest == null)
			return null;

		for (StudyTest st : studyTest) {
			StudyTestListRes s = new StudyTestListRes(st.getId(), st.getUser().getName(), st.getTitle());
			res.add(s);
		}

		return res;
	}

	@Override
	public List<StudyMemberInfoRes> getStudyMemberInfo(Long studyId) {
		List<UserStudy> userStudy = userStudyRepository.findByStudyId(studyId);
		if (userStudy == null)
			return null;

		List<StudyMemberInfoRes> res = new ArrayList<>();
		for (UserStudy us : userStudy) {
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
		if (!test.isPresent())
			return null;

		// 테이블 insert할 객체들
		score.setStudy(test.get().getStudy()); // 해당 스터디 저장
		score.setStudyTest(test.get()); // 문제집 저장
		Optional<User> user = userRepository.findById(userId); // 문제 푼 사람 저장
		if (user.isPresent())
			score.setUser(user.get());

		res.setNumber(test.get().getNumber()); // 문항 개수 세팅
		String[] testAnswer = test.get().getAnswer().split(","); // 정답 , 로 구분하기

		Short cCnt = 0; // 맞은 갯수
		for (int i = 0; i < testAnswer.length; i++) {
			if (answer.get(i) == null)
				continue;
			if (testAnswer[i].equals(answer.get(i))) {
				cCnt++;
			}
		}

		res.setCorrect(cCnt);
		score.setScore(cCnt);

		StudyScore origin = studyScoreRepository.findByStudyTestIdAndUserId(testId, userId);
		if (origin != null) {// 원래 점수가 있던 사람
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
		if (!st.isPresent())
			return null;

		// 정보 저장
		StudyTests test = new StudyTests(testId, st.get().getUser().getUserId(), st.get().getTitle(),
				st.get().getTest_url());
		// 반환 객체에 저장
		res.setTest(test);

		// 답 ,로 스플릿해서 저장
		String[] answer = st.get().getAnswer().split(",");
		res.setAnswer(answer);

		return res;
	}

	@Override
	public void updateStudy(Study study, MultipartFile studyThumbnail, StudyCreatePostReq studyInfo)
			throws ParseException, IllegalStateException, IOException {
		study.setContent(studyInfo.getContent());
//		String -> Date로 변환.
		study.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse(studyInfo.getDeadline()));
		study.setName(studyInfo.getName());
		study.setPopulation(studyInfo.getPopulation());
		study.setTemperature(studyInfo.getTemperature());
		study = studyRepository.save(study);
//		썸네일 저장하기
		String path;
//		만약 원래 썸네일이 존재하는 상태였다면, 그 사진부터 삭제해야 한다. 원래 디폴트 일 경우 제외..
		if(!isDefault(study.getStudy_thumbnail())) {
//			삭제해야 하는 파일(풀경로)
			Path filePath =  Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\"+study.getStudy_thumbnail());
			Files.deleteIfExists(filePath);
		}
		
		
//		새로운 사진 관련 처리 -> image/study/방번호-파일명
		path = BASE_PATH;
		if(studyThumbnail == null) {
//			디폴트 사진 처리
			path += "default.png";
		}
		else {
			path +="study\\"+study.getId() + "-" + studyThumbnail.getOriginalFilename();
			File dest = new File(path);
			studyThumbnail.transferTo(dest);
		}
		study.setStudy_thumbnail(getShortFilePath(path));
		study = studyRepository.save(study);
//		관심사 수정 처리
//		일단 내가 갖고 있는 관심사 다 out
		List<StudyInterest> studyInterestList = studyInterestRepository.findByStudyId(study.getId());
		if(studyInterestList != null) {
			for (StudyInterest si : studyInterestList) {
				studyInterestRepository.deleteById(si.getId());
			}
		}
//		새로운 관심사 in
		if (studyInfo.getInterest() != null) {
			for (String interest : studyInfo.getInterest()) {
//			만약 없다면, 관심사 테이블에 추가하고
				Interest i = interestRepository.findByInterest(interest);
				if (i == null) {
					i = new Interest();
					i.setName(interest);
					i = interestRepository.save(i);
				}
//			있던 관심사라면 관심사 테이블에 있는거 걍씀
//			study-interest 테이블에 추가
				studyInterestRepository.save(new StudyInterest(i, study));
			}
		}
	}

	@Override
	public List<StudyPlan> getPlansByStudy(Long studyId) {
		return studyPlanRepository.findByStudy_Id(studyId);
	}

	@Override
	public List<AttendGetRes> getAttendList(List<StudyPlan> studyPlans, List<User> members) {
//	사실상 members 배열은 컨트롤러에서 넘겨줄 필요가 없을 수도 ..?
	
		List<AttendGetRes> res = new ArrayList<>();
		
//		1. study_plan을 id 순으로 정렬
		Collections.sort(studyPlans, new Comparator<StudyPlan>() {
			@Override
			public int compare(StudyPlan o1, StudyPlan o2) {
				return Long.compare(o1.getId(), o2.getId());
			}
		});
		
//		2. 멤버 별로 객체에 담기 
		for(User m : members) {
			AttendGetRes attendGetRes = new AttendGetRes();
			attendGetRes.setUser_id(m.getId());
			attendGetRes.setUser_name(m.getName());
			Long mId = m.getId();
			//3.스터디 플랜 별로 출석 현황 질의
			for(StudyPlan sp : studyPlans) {
				AttendRes attendRes = new AttendRes();
				Long spId = sp.getId();
				StudyAttend at = studyAttendRepository.findByUserIdAndStudyPlanId(mId, spId);
				attendRes.setAttend(at.isAttend());
				attendRes.setStudy_plan_id(spId);
				attendRes.setStudy_plan_date(parseDate(sp.getDate()));
				attendGetRes.getAttendList().add(attendRes);
			}
			res.add(attendGetRes);
		}
		return res;
	}
	
//	날짜형 (시간 포함)을 문자열로
	private String parseDate(Date deadline) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		String str = dateFormat.format(deadline);
		
		return str;
	}
	
//	사진이 디폴트 이미지 인지 아닌지 판단
	private boolean isDefault(String filePath) {
//		image/이후부터 끝까지 == default.png면 디폴트 이미지임 (디폴트가 아닌 경우 폴더명 study가 중간에 추가됨)
		boolean isDefault = filePath.substring(6,filePath.length()).equals("default.png") ? true : false;
		return isDefault;
	}
	
	public List<StudyDetailInfo> getStudyDetailInfo(Long studyId) {
		List<StudyDetailInfo> res = new ArrayList<>();
		
		// 해당 스터디의 스터디 일정 가져오기
		List<StudyPlan> studyPlan = studyPlanRepository.findByStudy_Id(studyId);
		if(studyPlan == null) return null;
		
		for(StudyPlan sp: studyPlan) { // 일정에 따라 id, 제목, 날짜 넣기
			StudyDetailInfo detail = new StudyDetailInfo();
			
			detail.setStudy_plan_id(sp.getId());
			detail.setTitle(sp.getTitle());
			detail.setDate(sp.getDate());
			
			// 스터디 일정별 멤버 출석 현황
			// 스터디와 스터디 일정 번호로 출석 현황 가져오기
			List<StudyAttend> studyAttend = studyAttendRepository.findByStudyIdAndStudyPlanId(studyId, sp.getId());
			List<MemberAttend> member = new ArrayList<>(); // 멤버 출석 현황 저장
			for(StudyAttend sa: studyAttend) {
				MemberAttend ma = new MemberAttend();
				ma.setAttend(sa.isAttend());
				ma.setUser_id(sa.getUser().getId());
				member.add(ma);
			}
			
			detail.setStudyAttend(member);
			
			res.add(detail);
		}
		
		return res;
	}

	@Override
	public String postStudyAttend(Long studyId, Long userId) {
		
		try {
			// 현재 스터디 일정
			DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date nowDate = new Date();
			String today = sdFormat.format(nowDate); // 오늘 날짜
			Date to = sdFormat.parse(today);

			StudyPlan studyPlan = studyPlanRepository.findByDate(to);
			if(studyPlan == null) // 오늘 스터디 없을 경우
				return "date";
					
			StudyAttend sa = studyAttendRepository.findByStudyIdAndUserIdAndStudyPlanId(studyId, userId, studyPlan.getId());
			if(sa == null)
				return "error";
			
			sa.setAttend(true);
			studyAttendRepository.save(sa);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ok";
	}
}
