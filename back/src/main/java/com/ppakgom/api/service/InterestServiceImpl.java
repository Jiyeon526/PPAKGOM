package com.ppakgom.api.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppakgom.api.request.InviteReq_receiver;
import com.ppakgom.api.request.InviteReq_sender;
import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyApply;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.repository.InterestRepository;
import com.ppakgom.db.repository.StudyApplyRepository;
import com.ppakgom.db.repository.StudyRepository;

@Service("InterestService")
public class InterestServiceImpl implements InterestService {

	@Autowired
	InterestRepository interestRepository;
	
	@Override
	public List<Interest> getInterestByName(String interest) {
		return interestRepository.findAllByName(interest);
	}




}
