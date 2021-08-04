package com.ppakgom.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.Interest;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

	Optional<Interest> findByName(String s);
	void save(String s);

}
