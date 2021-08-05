package com.ppakgom.api.response;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("스터디 상세 결과")
public class StudySearchDetailGetRes {
	ArrayList<StudyDetailRes> studyResult;

}
