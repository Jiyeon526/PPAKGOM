package com.ppakgom.api.service;

import java.util.List;

import com.ppakgom.api.response.JoinApplyListRes;

public interface JoinService {

	List<JoinApplyListRes> getJoinApplyList(Long user_id);

}
