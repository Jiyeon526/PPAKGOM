package com.ppakgom.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppakgom.db.entity.StudyAttend;

public interface StudyAttendRepository extends JpaRepository<StudyAttend, Long> {

	List<StudyAttend> findByStudyIdAndStudyPlanId(Long studyId, Long id);

	StudyAttend findByStudyIdAndUserIdAndStudyPlanId(Long studyId, Long userId, Long id);

	StudyAttend findByUserIdAndStudyPlanId(Long userId, Long studyPlanId);


}
