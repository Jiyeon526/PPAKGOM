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
@ApiModel("스터디 검색 결과")
public class StudySearchGetRes{
	
	ArrayList<Study> studyResult;
}
