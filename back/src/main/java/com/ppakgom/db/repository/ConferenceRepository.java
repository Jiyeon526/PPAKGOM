package com.ppakgom.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ppakgom.api.response.conferenceList.ConferenceContent;
import com.ppakgom.db.entity.Conference;

public interface ConferenceRepository extends JpaRepository<Conference, Long>{
	@Transactional
    void deleteAllByUserId(Long userId);
//	@Query("select * from conference where title := ?1")
	@Query
	List<Conference> findConferencesByTitle(String Title);

}
