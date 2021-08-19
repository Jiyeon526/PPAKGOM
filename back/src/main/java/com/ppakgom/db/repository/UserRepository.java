package com.ppakgom.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

	// 아이디로 사용자 찾기
	@Query(value = "SELECT * FROM USER WHERE BINARY(USER_ID) = ?1",nativeQuery = true) 
	User findUserById(String userId);
	
	// 닉네임으로 사용자 찾기
	@Query(value = "SELECT * FROM USER WHERE BINARY(NAME) = ?1", nativeQuery = true)
	Optional<User> findByName(String name);

//	순번 아이디(PK)로 사용자 찾기
	User findUserById(Long userId);
	
	@Query(value = "SELECT * FROM USER WHERE BINARY(EMAIL) = ?1", nativeQuery = true)
	User findTop1ByEmail(String email);

}
