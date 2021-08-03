package com.ppakgom.api.response;

import java.util.List;

import com.ppakgom.api.response.conferenceList.*;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ConferenceListRes")
public class ConferenceListRes {
	List<ConferenceContent> content;
	boolean empty;
	boolean first;
	boolean last;
	int number;
	int numberOfElements;
	Pageable pageable;
	int size;
	Sort sort;
	int totalElements;
	int totalPages;
	
}
