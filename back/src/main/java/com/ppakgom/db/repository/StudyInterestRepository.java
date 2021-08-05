package com.ppakgom.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.StudyInterest;

@Repository
public interface StudyInterestRepository extends JpaRepository<StudyInterest,Long>{

	@Query(value = "SELECT STUDY_ID FROM STUDY_INTEREST WHERE INTEREST_ID = ?1",nativeQuery = true)
	List<Long> findByInterestId(Long id);

//	@Query(value = "SELECT INTEREST_ID FROM STUDY_INTEREST WHERE STUDY_ID = ?1",nativeQuery = true)
//	List<Object> findByStudyId(Long id);

	List<StudyInterest> findByStudyId(Long study_id);

}
