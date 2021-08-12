package com.ppakgom.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppakgom.db.entity.StudyTest;

public interface StudyTestRepository extends JpaRepository<StudyTest, Long> {

	List<StudyTest> findByStudy_Id(Long studyId);

}
