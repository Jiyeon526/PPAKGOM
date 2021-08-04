package com.ppakgom.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;

@Repository
public interface InterestRepository extends JpaRepository<Interest,Long>{

	@Query(value = "SELECT * FROM INTEREST WHERE NAME = ?1",nativeQuery = true)
	Interest findByName(String interest);
	
}
