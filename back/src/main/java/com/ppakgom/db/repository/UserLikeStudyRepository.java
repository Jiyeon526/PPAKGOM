package com.ppakgom.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserLikeStudy;

@Repository
public interface UserLikeStudyRepository extends JpaRepository<UserLikeStudy,Long>{

	List<UserLikeStudy> findByUserId(Long userId);

}
