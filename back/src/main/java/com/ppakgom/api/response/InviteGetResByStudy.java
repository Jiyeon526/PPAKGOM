package com.ppakgom.api.response;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("초대 회원 현황")
public class InviteGetResByStudy {
	
	ArrayList<InviteResByStudy> inviteResult;
	
	public InviteGetResByStudy() {
		this.setInviteResult(new ArrayList<>());
	}
}
