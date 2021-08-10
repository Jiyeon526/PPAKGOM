package com.ppakgom.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppakgom.db.entity.StudyScore;

public interface StudyScoreRepository extends JpaRepository<StudyScore, Long> {

	Optional<StudyScore> findByUserIdAndStudyTestId(Long userId, Long testId);

}
