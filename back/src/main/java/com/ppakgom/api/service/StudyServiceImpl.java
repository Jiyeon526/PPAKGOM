package com.ppakgom.api.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyInterest;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserLikeStudy;
import com.ppakgom.db.entity.UserStudy;
import com.ppakgom.db.repository.InterestRepository;
import com.ppakgom.db.repository.StudyInterestRepository;
import com.ppakgom.db.repository.StudyRepository;
import com.ppakgom.db.repository.UserLikeStudyRepository;
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
	UserLikeStudyRepository userLikeStudyRepository;

	String BASE_PATH;
	
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
		BASE_PATH = System.getProperty("user.dir");
		BASE_PATH += "\\src\\main\\resources\\image\\study\\";
		/* 경로만 출력시키면 에러가 사라지는 매직... */
//		System.out.println(BASE_PATH);
		String path = BASE_PATH + studyId + "-" + studyThumbnail.getOriginalFilename();
//		System.out.println(path);
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

}
