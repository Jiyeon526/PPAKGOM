package com.ppakgom.api.response;

import java.util.ArrayList;
import java.util.Date;

import com.ppakgom.db.entity.Conference;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Conference Info Detail Response")
public class ConferenceInfoDetailRes {
	@ApiModelProperty(name="Conference ID", example="1")
	Long id;
	@ApiModelProperty(name="Conference Category", example="교육")
	String conference_category;
	@ApiModelProperty(name="Owner name", example="User-1")
	String owner;
	@ApiModelProperty(name="Start Time", example="DateTime")
	Date callStartTime;
	@ApiModelProperty(name="Thumbnail URL", example="/1/image.png")
	String thumbnail_url;
	@ApiModelProperty(name="Title", example="방 제목")
	String title;
	@ApiModelProperty(name="Description", example="방 설명")
	String description;
	@ApiModelProperty(name="Active", example="true")
	boolean is_active;
	@ApiModelProperty(name="Users", example="")
	ArrayList<ConferenceUsers> users;
	
	public static ConferenceInfoDetailRes of(Conference conference, String c_category, String owner, ArrayList<ConferenceUsers> users) {
		ConferenceInfoDetailRes res = new ConferenceInfoDetailRes();
		res.setId(conference.getId());
		res.setCallStartTime(conference.getCallStartTime());
		res.setThumbnail_url(conference.getThumbnailUrl());
		res.setTitle(conference.getTitle());
		res.setDescription(conference.getDescription());
		res.set_active(conference.isActive());
		res.setUsers(users);
		res.setConference_category(c_category);
		res.setOwner(owner);
		
		return res;
	}
	
}
