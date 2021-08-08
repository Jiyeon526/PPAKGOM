package com.ppakgom.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.StudyRate;
import com.ppakgom.db.entity.StudyRateId;

@Repository
public interface StudyRateRepository extends JpaRepository<StudyRate,StudyRateId>{

//	@Query(value = "SELECT STUDY_ID FROM STUDY_INTEREST WHERE INTEREST_ID = ?1",nativeQuery = true)
//	List<Long> findByInterestId(Long id);

//	@Query(value = "SELECT INTEREST_ID FROM STUDY_INTEREST WHERE STUDY_ID = ?1",nativeQuery = true)
//	List<Object> findByStudyId(Long id);

//	List<StudyInterest> findByStudyId(Long study_id);

}
