package com.ppakgom.db.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ppakgom.db.entity.StudyPlan;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {

	List<StudyPlan> findByStudy_Id(Long studyId);

	StudyPlan findByDateAndStudyId(Date to, Long study_id);

}
