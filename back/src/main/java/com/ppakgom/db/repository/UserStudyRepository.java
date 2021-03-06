package com.ppakgom.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.User;
import com.ppakgom.db.entity.UserStudy;

@Repository
public interface UserStudyRepository extends JpaRepository<UserStudy, Long> {

	@Query(value = "SELECT COUNT(*) FROM USER_STUDY WHERE STUDY_ID = ?1", nativeQuery = true)
	int getJoinedUserByStudyId(Long id);

	List<UserStudy> findByUserId(Long userId);

	List<UserStudy> findByStudyId(Long studyId);

	Optional<UserStudy> findUserStudyByUserIdAndStudyId(Long userId, Long studyId);

}
