package com.ppakgom.api.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.ConferenceCategoryRegisterPostReq;
import com.ppakgom.api.request.ConferenceModifyReq;
import com.ppakgom.api.request.ConferenceRegisterPostReq;
import com.ppakgom.api.response.ConferenceListRes;
import com.ppakgom.api.response.conferenceList.ConferenceContent;
import com.ppakgom.api.response.conferenceList.Sort;
import com.ppakgom.db.entity.Conference;
import com.ppakgom.db.entity.ConferenceCategory;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.repository.ConferenceCategoryRepository;
import com.ppakgom.db.repository.ConferenceRepository;

@Service("conferenceService")
public class ConferenceServiceImpl implements ConferenceService {

	@Autowired
	ConferenceCategoryRepository conferenceCategoryRepository;
	@Autowired
	ConferenceRepository conferenceRepository;
	

	@Override
	public void createConferenceCategory(ConferenceCategoryRegisterPostReq conferenceCategoryRegisterPostReq) {
		ConferenceCategory conference = new ConferenceCategory();
		conference.setName(conferenceCategoryRegisterPostReq.getName());
		conferenceCategoryRepository.save(conference);
	}
	
	@Override
	public List<ConferenceCategory> getConferenceCategories() {
		return conferenceCategoryRepository.getConferenceCategories();

	}

	@Override
	public Conference createConference(ConferenceRegisterPostReq registerInfo, User user, MultipartFile thumbnail)
			throws IllegalStateException, IOException {
		Conference conference = new Conference();
//		방장, 활성화 상태 설정.
		conference.setUser(user);
		conference.setActive(true);
//		카테고리 정보 구하기.
		ConferenceCategory conferenceCategory = conferenceCategoryRepository
				.getConferenceCategoryById(registerInfo.getConference_category());
		conference.setConferenceCategory(conferenceCategory);
//		방 설명, 방 제목 저장하기.
		conference.setDescription(registerInfo.getDescription());
		conference.setTitle(registerInfo.getTitle());

//		썸네일 저장하기.
//		일단 회의실 먼저 저장하고
		Long cId = conferenceRepository.save(conference).getId();

		String absolutePath = "C:/Users/multicampus/Documents/GitHub/temp/S05P12B306/backend/src/main/resources/image";
//        그 아이디 값이용하기
		String filePath = absolutePath + "/" + cId + "-" + thumbnail.getOriginalFilename();
		File dest = new File(filePath);
		thumbnail.transferTo(dest); // 파일 업로드 작업 수행

		conference.setThumbnailUrl(filePath);
		conferenceRepository.save(conference);

		return conference;
	}

	@Override
	public ConferenceListRes getConferenceListRes(String title, String titleSort, String timeSort, int page, int size,
			Long conference_category) {
		ConferenceListRes conferenceListRes = new ConferenceListRes();

//		boolean first;
//		boolean last;
//		int number;
//		int numberOfElements;
//		Pageable pageable;
//		int size;
//		Sort sort;
//		int totalElements;
//		int totalPages;

//		** setContent(방 검색)
//		1-1. title로 검색 -> Conference로 반환
		List<Conference> conferences;
		Sort sort = new Sort();
		
		if (title != null) {
			conferences = findConferences(title, titleSort, timeSort);
		}
		else {
			conferences = findConferences(titleSort, timeSort);
		}

		List<ConferenceContent> conferenceContents = new ArrayList<>();
//		1-2.결과가 null인지 확인.-> ** setEmpty
		
		if ( conferences.size() != 0) {
			conferenceListRes.setEmpty(false);
			ConferenceContent cc = new ConferenceContent();
			System.out.println("컨퍼런스 배열 크기: ");
			
//		1-3.ConferenceContent로 변환(of 사용)
			for (Conference c : conferences) {
				conferenceContents.add(cc.of(c));
			}
		}
		
		else {
			conferenceListRes.setEmpty(true);
		}
		

		
		conferenceListRes.setContent(conferenceContents);

		
		
		
		return conferenceListRes;
	}
	
//	검색어 없을 때
	private List<Conference> findConferences(String titleSort, String timeSort) {
		return conferenceRepository.findAll();
	}
	
//	검색어 있을 때
	private List<Conference> findConferences(String title, String titleSort, String timeSort) {
		
//		검색 조건 두개.
//		if(titleSort != null && timeSort != null) {
//			conferenceRepository.findConferencesWithCodition(titleSort, timeSort,title);
//		}
////		검색 조건 없음.
//		else if(titleSort  == null && timeSort == null) {
//			conferenceRepository.findConferencesByTitle(title);
//		}
////		검색 조건 하나.
//		else {
//			String col = titleSort.length() == 0 ? "title" : "time";
//			conferenceRepository.findConferencesByTitle(title,col,)
//		}
		return conferenceRepository.findConferencesByTitle(title);
	}

	@Override
	public Conference getConferenceInfo(Long conference_id) {
		Optional<Conference> res = conferenceRepository.findById(conference_id);
		
		if(!res.isPresent())
			return null;
		
		return res.get();
	}

	@Override
	public String getCategory(Long id) {
		Optional<ConferenceCategory> res = conferenceCategoryRepository.findById(id);
		if(!res.isPresent())
			return null;
		return res.get().getName();
	}

	@Override
	public boolean modifyConferenceInfo(Conference conference, ConferenceModifyReq req, MultipartFile file) {
		
		try {
			Optional<ConferenceCategory> change_category = conferenceCategoryRepository.findById(req.getConference_category());
			
			if(change_category.isPresent())
				conference.setConferenceCategory(change_category.get());
			
			conference.setTitle(req.getTitle());
			conference.setDescription(req.getDescription());

			Path deleteFilePath = Paths.get(conference.getThumbnailUrl());
			Files.deleteIfExists(deleteFilePath);
			
			String basePath = "C:/Users/multicampus/Documents/GitHub/temp/S05P12B306/backend/src/main/resources/image";
			String filePath = basePath + "/" + conference.getId() + "-" + file.getOriginalFilename();
			File dest = new File(filePath);
			file.transferTo(dest);
			
			if(!dest.exists()) {
				System.out.println("파일 업로드 실패");
				return false;
			}
			
			conference.setThumbnailUrl(filePath);
			conferenceRepository.save(conference);
		} catch (IOException e) {
			System.out.println("예외처리 발생");
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
