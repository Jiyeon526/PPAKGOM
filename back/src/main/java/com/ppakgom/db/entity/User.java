package com.ppakgom.db.entity;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User extends BaseEntity{
	
    String userId;
    String email;
    String name;
    String platform_type;
    String access_token;
    String profile_thumbnail;
    float temperature;
    String position;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

	@Override
	public String toString() {
		return "User [userId=" + userId + ", email=" + email + ", name=" + name + ", platform_type=" + platform_type
				+ ", access_token=" + access_token + ", profile_thumbnail=" + profile_thumbnail + ", temperature="
				+ temperature + ", position=" + position + ", password=" + password + "]";
	}

    
    
}

