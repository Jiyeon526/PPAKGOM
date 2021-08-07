package com.ppakgom.db.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.UserInterest;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
	
	@Query(value = "SELECT INTEREST_ID FROM USER_INTEREST WHERE USER_ID = ?1",nativeQuery = true) 
	List<Long> findByInterestId(Long userid);
	
	@Transactional
	@Modifying
	@Query(value = "delete from user_interest where user_id= :user_id and interest_id= :interest_id",nativeQuery = true)
	void deleteUserInterest(@Param("user_id") Long user_id,@Param("interest_id") Long interest_id);

}
