package com.ppakgom.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserInterest;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
	
	@Query(value = "SELECT INTEREST_ID FROM USER_INTEREST WHERE USER_ID = ?1",nativeQuery = true) 
	List<Long> findByInterestId(Long userid);
	
	List<UserInterest> findUserInterestByInterestId(Long interestId);

	List<UserInterest> findInterestByUserId(Long userId);


}
