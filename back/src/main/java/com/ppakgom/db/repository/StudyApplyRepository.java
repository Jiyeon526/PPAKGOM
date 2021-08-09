package com.ppakgom.db.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.Interest;
import com.ppakgom.db.entity.Study;
import com.ppakgom.db.entity.StudyApply;
import com.ppakgom.db.entity.User;

@Transactional
@Repository
public interface StudyApplyRepository extends JpaRepository<StudyApply,Long>{

	List<StudyApply> findBySenderId(Long senderId);

//	List<StudyApply> findByStudyId(Long studyId);

//	@Query(value = "SELECT * FROM STUDY_APPLY WHERE RECEIVER_ID = ?1 AND STATE = 2",nativeQuery = true )
//	List<StudyApply> findByReceiverId(Long receiverId);


//	void deleteBySenderIdAndStudyIdAndReceiverId(Long senderId, Long studyId, Long receiverId);

//	StudyApply findByReceiverIdAndStudyIdAndSenderId(Long receiverId, Long studyId, Long senderId);

//	void deleteBySenderIdAndStudyIdAndReceiverIdAndState(Long senderId, Long studyId, Long receiverId, short state);

	List<StudyApply> findBySenderIdAndIsJoin(Long userId, boolean isJoin);

//	상태가 '대기'일 때만 보여줌.
	@Query(value = "SELECT * FROM STUDY_APPLY WHERE RECEIVER_ID = ?1 AND IS_JOIN = ?2 AND STATE = 2",nativeQuery = true )
	List<StudyApply> findByReceiverIdAndIsJoin(Long userId, boolean isJoin);

	void deleteBySenderIdAndStudyIdAndReceiverIdAndIsJoin(Long userId, Long studyId, Long receiverId, boolean isJoin);

	StudyApply findByReceiverIdAndStudyIdAndSenderIdAndIsJoin(Long userId, Long studyId, Long senderId, boolean isJoin);

	void deleteBySenderIdAndStudyIdAndReceiverIdAndStateAndIsJoin(Long userId, Long studyId, Long receiverId, short state,boolean isJoin);

	List<StudyApply> findByStudyIdAndIsJoin(Long studyId, boolean isJoin);


}
