package com.ppakgom.api.response;

import com.ppakgom.common.model.response.BaseResponseBody;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 방 생성 API ([POST] /api/v1/conferences/conferences) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@ApiModel("ConferenceRegisterPostResponse")
public class ConferenceRegisterPostRes extends BaseResponseBody{
	@ApiModelProperty(name="생성된 컨퍼런스 ID", example="1")
	Long id;
	
	public static ConferenceRegisterPostRes of(Integer statusCode, String message, Long id) {
		ConferenceRegisterPostRes res = new ConferenceRegisterPostRes();
		res.setStatusCode(statusCode);
		res.setMessage(message);
		res.setId(id);
		return res;
	}
}
