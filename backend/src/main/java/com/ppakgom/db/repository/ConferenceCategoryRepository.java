package com.ppakgom.db.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.ConferenceCategory;

@Repository
public interface ConferenceCategoryRepository extends JpaRepository<ConferenceCategory, Long> {
    @Query(value="select * from conference_category",nativeQuery = true)
	List<ConferenceCategory> getConferenceCategories();
    
    @Query(value="select * from conference_category where id=?1",nativeQuery = true)
	ConferenceCategory getConferenceCategoryById(Long conference_category);

}
