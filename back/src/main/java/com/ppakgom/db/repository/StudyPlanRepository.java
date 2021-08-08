package com.ppakgom.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppakgom.db.entity.StudyPlan;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {

	List<StudyPlan> findByStudy_Id(Long studyId);

}
