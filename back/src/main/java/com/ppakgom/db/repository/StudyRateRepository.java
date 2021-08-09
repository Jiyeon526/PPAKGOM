package com.ppakgom.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.StudyRate;

@Repository
public interface StudyRateRepository extends JpaRepository<StudyRate,Long>{


}
