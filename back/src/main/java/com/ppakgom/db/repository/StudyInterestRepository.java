package com.ppakgom.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.StudyInterest;

@Repository
public interface StudyInterestRepository extends JpaRepository<StudyInterest,Long>{

}
