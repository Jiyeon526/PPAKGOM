package com.ppakgom.api.response;

import java.util.ArrayList;


import com.ppakgom.common.model.response.BaseResponseBody;
import com.ppakgom.db.entity.BaseEntity;
import com.ppakgom.db.entity.Study;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("해당 유저가 보낸 초대 현황")
public class InviteGetRes{
	
	ArrayList<InviteRes> inviteResult;
	
	public InviteGetRes() {
		this.setInviteResult(new ArrayList<>());
	}

}
