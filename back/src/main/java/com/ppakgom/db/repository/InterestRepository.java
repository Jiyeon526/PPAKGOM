package com.ppakgom.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppakgom.db.entity.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> {

	Optional<Interest> findByName(String s);
	void save(String s);
	Long findCountId();
	
}
