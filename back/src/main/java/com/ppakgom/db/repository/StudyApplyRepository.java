package com.ppakgom.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ppakgom.db.entity.StudyApply;

public interface StudyApplyRepository extends JpaRepository<StudyApply, Long> {

//	@Query("SELECT * FROM STUDY_APPLY WHERE IS_JOIN = 1 AND SENDER_ID = ?1",nativeQuery = true)
	List<StudyApply> findBySender_Id(Long sender_id);

	StudyApply findBySender_IdAndStudy_IdAndReceiver_Id(Long userid, Long study_id, Long receiver_id);

	List<StudyApply> findByReceiver_Id(Long userid);

}
