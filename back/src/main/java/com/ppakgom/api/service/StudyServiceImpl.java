package com.ppakgom.api.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyInterest;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.repository.InterestRepository;
import com.ppakgom.db.repository.StudyInterestRepository;
import com.ppakgom.db.repository.StudyRepository;

@Service("StudyService")
public class StudyServiceImpl implements StudyService {

	@Autowired
	StudyRepository studyRepository;

	@Autowired
	InterestRepository interestRepository;

	@Autowired
	StudyInterestRepository studyInterestRepository;

	@Override
	public Study createStudy(StudyCreatePostReq studyInfo, User user, MultipartFile studyThumbnail)
			throws IllegalStateException, IOException {

		Study study = new Study();

		study.setName(studyInfo.getName());
		study.setContent(studyInfo.getContent());
		study.setTemperature(studyInfo.getTemperature());
		study.setPopulation(studyInfo.getPopulation());
		study.setDeadline(studyInfo.getDeadline());

//		1. 관심사 테이블.
		ArrayList<Long> interestId = new ArrayList<Long>();
		String[] interests = studyInfo.getInterest();
		for (String interest : interests) {
//			중복 검사 실시 -> 중복한다면 그 값을 가져오고, 중복하지 않는다면 insert해서 id를 가져온다.
			Interest i;
			try {
				i = interestRepository.findByInterest(interest);

			} catch (NoSuchElementException e) {
				i = interestRepository.save(new Interest(interest));
			}
			interestId.add(i.getId());
		}

//		2. 스터디 id 뽑기.
		Long studyId = studyRepository.save(study).getId();

//		3. 사진 관련 처리 -> image/study/방번호-파일명
		String path = "C:/Users/multicampus/Desktop/real3rdGit/S05P13B306/back/src/main/resources/image/study";
		path += "/" + studyId + "-" + studyThumbnail.getOriginalFilename();
		File dest = new File(path);
		studyThumbnail.transferTo(dest);
		study.setStudy_thumbnail(getShortFilePath(path));

//		4. 관심사 - 스터디 테이블.
		for (Long iId : interestId) {
			studyInterestRepository.save(new StudyInterest(studyId, iId));
		}
		return studyRepository.save(study);
	}

	private String getShortFilePath(String path) {
		int idx = path.indexOf("image");
		return path.substring(idx, path.length());

	}

}