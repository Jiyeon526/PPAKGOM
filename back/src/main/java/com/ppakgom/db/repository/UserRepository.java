package com.ppakgom.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	// 아이디로 사용자 찾기
	 @Query(value = "SELECT * FROM USER WHERE USER_ID = ?1",nativeQuery = true) 
	 User findUserById(String userId);
}
