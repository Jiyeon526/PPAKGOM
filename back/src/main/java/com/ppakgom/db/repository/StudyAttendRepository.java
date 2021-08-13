package com.ppakgom.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppakgom.db.entity.StudyAttend;

public interface StudyAttendRepository extends JpaRepository<StudyAttend, Long> {

	StudyAttend findByUserIdAndStudyPlanId(Long userId, Long studyPlanId);

}