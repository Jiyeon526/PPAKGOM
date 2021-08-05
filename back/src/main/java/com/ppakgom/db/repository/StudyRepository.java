package com.ppakgom.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;

@Repository
public interface StudyRepository extends JpaRepository<Study,Long>{

	@Query(value = "SELECT * FROM STUDY WHERE NAME Like %?1%",nativeQuery = true)
	List<Study> findByName(String name);
}
