package com.ppakgom.db.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ppakgom.db.entity.StudyPlan;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {

	List<StudyPlan> findByStudy_Id(Long studyId);
	
//	@Query(value = "SELECT * FROM STUDY_PLAN WHERE DATE(date) = 1?", nativeQuery = true)
	StudyPlan findByDate(Date date);

}
