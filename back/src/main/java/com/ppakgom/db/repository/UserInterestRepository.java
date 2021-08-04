package com.ppakgom.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.UserInterest;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

}
