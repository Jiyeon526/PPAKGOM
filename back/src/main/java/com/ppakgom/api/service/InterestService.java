package com.ppakgom.api.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.ppakgom.api.request.InviteReq_receiver;
import com.ppakgom.api.request.InviteReq_sender;
import com.ppakgom.api.request.StudyCreatePostReq;
import com.ppakgom.api.request.StudyRatePostReq;
import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyApply;
import com.ppakgom.db.entity.User;

public interface InterestService {

	List<Interest> getInterestByName(String interest);

	
}
