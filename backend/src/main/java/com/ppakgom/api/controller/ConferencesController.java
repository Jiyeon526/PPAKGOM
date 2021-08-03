package com.ppakgom.api.controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.io.File;

import javax.swing.filechooser.FileSystemView;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.ConferenceModifyReq;
import com.ppakgom.api.request.ConferenceRegisterPostReq;
import com.ppakgom.api.request.Sort;
import com.ppakgom.api.request.UserRegisterPostReq;
import com.ppakgom.api.response.ConferenceCategoryRes;
import com.ppakgom.api.response.ConferenceInfoDetailRes;
import com.ppakgom.api.response.ConferenceListRes;
import com.ppakgom.api.response.ConferenceRegisterPostRes;
import com.ppakgom.api.response.ConferenceUsers;
import com.ppakgom.api.response.UserRes;
import com.ppakgom.api.response.conferenceList.ConferenceContent;
import com.ppakgom.api.response.conferenceList.Pageable;
import com.ppakgom.api.service.ConferenceService;
import com.ppakgom.api.service.UserService;
import com.ppakgom.common.auth.SsafyUserDetails;
import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.Conference;
import com.ppakgom.db.entity.ConferenceCategory;
import com.ppakgom.db.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.http.HttpRequest;

/**
 * 회의실 관련 API 요청 처리를 위한 컨트롤러 정의.
 */

@Api(value = "conferences API", tags = { "Conference" })
@RestController
@RequestMapping("/api/v1/conferences")
@CrossOrigin
public class ConferencesController {

	@Autowired
	ConferenceService conferenceService;

	@Autowired
	UserService userService;

	@GetMapping("/conference-categories")
	@ApiOperation(value = "방 카테고리 조회", notes = "방 카테고리 목록을 반환")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공"), @ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<List<ConferenceCategoryRes>> getConferenceCategories() {
		ArrayList<ConferenceCategoryRes> res = new ArrayList<>();

		List<ConferenceCategory> categories = conferenceService.getConferenceCategories();
		for (ConferenceCategory c : categories)
			res.add(new ConferenceCategoryRes().of(c));

		return ResponseEntity.status(200).body(res);
	}

	@GetMapping(value = "/conferences")
	@ApiOperation(value = "방 목록", notes = "방 목록을 쿼리에 따라 반환한다.")
//	인자: title, sort, page, size, conference_category
	public ConferenceListRes getConferenceList(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "titleSort", required = false) String titleSort,
			@RequestParam(value = "timeSort", required = false) String timeSort, @RequestParam(value = "page") int page,
			@RequestParam(value = "size") int size, @RequestParam("conference_category") Long conference_category) {

		ConferenceListRes conferenceListRes = conferenceService.getConferenceListRes(title, titleSort, timeSort, page,
				size, conference_category);
		return conferenceListRes;
	}

	@GetMapping("/{conference_id}")
	@ApiOperation(value = "방 상세 정보 조회", notes = "conference_id를 통해 방 상세 정보 조회를 한다.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공"), @ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<ConferenceInfoDetailRes> getRoomInfoDetail(
			@PathVariable @ApiParam(value = "conference ID", required = true) Long conference_id) {

		Conference conference = conferenceService.getConferenceInfo(conference_id);

		if (conference == null)
			return new ResponseEntity<ConferenceInfoDetailRes>(HttpStatus.BAD_REQUEST);

		String category = conferenceService.getCategory(conference.getConferenceCategory().getId());
		String owner = conference.getUser().getUserId();
		ArrayList<ConferenceUsers> user = userService.getConferenceUserInfo(conference.getId());

		return ResponseEntity.status(200).body(ConferenceInfoDetailRes.of(conference, category, owner, user));
	}

	@PostMapping(value = "/conferences")
	@ApiOperation(value = "방 생성", notes = "방을 만든 뒤, 만들어진 방의 ID를 반환한다.", consumes = "multipart/form-data", produces = "multipart/form-data")
	public ResponseEntity<ConferenceRegisterPostRes> register(
			@ApiParam(value = "생성할 방 정보", required = true) ConferenceRegisterPostReq registerInfo,
			@RequestPart("thumbnail") MultipartFile thumbnail, @ApiIgnore Authentication authentication) {

//		로그인한 사용자가 owner_id 가 됨.
		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		String userId = userDetails.getUsername();
		User user = userService.getUserByUserId(userId);
		Conference conference = new Conference();
		try {

			conference = conferenceService.createConference(registerInfo, user, thumbnail);
		} catch (Exception e) {
			System.err.println("파일 저장 에러");
			e.printStackTrace();
			return ResponseEntity.status(500).body(ConferenceRegisterPostRes.of(500, "Fail", 0L));
		}
		return ResponseEntity.status(201).body(ConferenceRegisterPostRes.of(201, "Success", conference.getId()));
	}

	@PutMapping("/{conference_id}")
	@ApiOperation(value = "방 정보 수정", notes = "conference_id를 통해 방 정보를 수정한다.", consumes = "multipart/form-data", produces = "multipart/form-data")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공"), @ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"), @ApiResponse(code = 500, message = "서버 오류") })
	public ResponseEntity<BaseResponseBody> modifyRoomInfo(ConferenceModifyReq req,
			@RequestPart("thumbnail") MultipartFile file,
			@PathVariable @ApiParam(value = "conference ID", required = true) Long conference_id,
			@ApiIgnore Authentication authentication) {

		Conference conference = conferenceService.getConferenceInfo(conference_id);

		SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
		Long authUserId = userDetails.getUser().getId();

		if (authUserId != conference.getUser().getId())
			return ResponseEntity.status(404).body(BaseResponseBody.of(404, "Fail"));

		if (conference == null)
			return ResponseEntity.status(404).body(BaseResponseBody.of(404, "Fail"));

		if (conferenceService.modifyConferenceInfo(conference, req, file)) {
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
		}

		return ResponseEntity.status(404).body(BaseResponseBody.of(404, "Fail"));
	}
}
