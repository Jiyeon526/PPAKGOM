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
import java.util.Optional;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.api.request.StudyRatePostReq;
import com.ppakgom.api.request.WorkbookCreatePostReq;
import com.ppakgom.api.response.StudyScheduleMonthRes;

import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyInterest;
import com.ppakgom.db.entity.StudyRate;
import com.ppakgom.db.entity.StudyTest;
import com.ppakgom.db.entity.StudyPlan;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserLikeStudy;
import com.ppakgom.db.entity.UserStudy;
import com.ppakgom.db.repository.InterestRepository;
import com.ppakgom.db.repository.StudyInterestRepository;
import com.ppakgom.db.repository.StudyRateRepository;
import com.ppakgom.db.repository.StudyPlanRepository;
import com.ppakgom.db.repository.StudyRepository;
import com.ppakgom.db.repository.StudyTestRepository;
import com.ppakgom.db.repository.UserLikeStudyRepository;
import com.ppakgom.db.repository.UserRepository;
import com.ppakgom.db.repository.UserStudyRepository;

@Service("StudyTestService")
public class StudyTestServiceImpl implements StudyTestService {

	@Autowired
	StudyTestRepository studyTestRepository;

	String BASE_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\test\\";

//    String test_url;

	@Override
	public void createWorkbook(Study study, User writer, WorkbookCreatePostReq workbookInfo, MultipartFile testFile) throws IllegalStateException, IOException {
		StudyTest st = new StudyTest();

//		1. db에 저장위해 값 세팅
		st.setStudy(study);
		st.setUser(writer);
		String[] answers = workbookInfo.getAnswer();
		st.setNumber((short) answers.length);
		st.setTitle(workbookInfo.getTest().getTitle());

//		answer-> 배열로 나오므로 이걸 뽑아서 ,로 이어 붙여야 함
		StringBuilder sb = new StringBuilder("");

		for (String ans : answers) {
			sb.append(ans).append(",");
		}

		sb.setLength(sb.length() - 1);
		st.setAnswer(sb.toString());

//		2. db저장해서 아이뒤 뽑아내기 -> 그걸로 url 설정
		st = studyTestRepository.save(st);

		String path = BASE_PATH + st.getId() + "-" + testFile.getOriginalFilename();
		st.setTest_url(getShortPath(path));
		
//		3. 스터디 파일 저장
		File dest = new File(path);
		testFile.transferTo(dest);

		studyTestRepository.save(st);

	}

	private String getShortPath(String path) {
		int idx = path.indexOf("test");
		return path.substring(idx, path.length());
	}

}
