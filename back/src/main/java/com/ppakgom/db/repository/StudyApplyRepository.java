package com.ppakgom.db.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ppakgom.db.entity.StudyApply;

@Transactional
@Repository
public interface StudyApplyRepository extends JpaRepository<StudyApply,Long>{

	List<StudyApply> findBySenderId(Long senderId);

	List<StudyApply> findBySenderIdAndIsJoin(Long userId, boolean isJoin);

//	상태가 '대기'일 때만 보여줌.
	@Query(value = "SELECT * FROM STUDY_APPLY WHERE RECEIVER_ID = ?1 AND IS_JOIN = ?2 AND STATE = 2",nativeQuery = true )
	List<StudyApply> findByReceiverIdAndIsJoin(Long userId, boolean isJoin);

	void deleteBySenderIdAndStudyIdAndReceiverIdAndIsJoin(Long userId, Long studyId, Long receiverId, boolean isJoin);

	StudyApply findByReceiverIdAndStudyIdAndSenderIdAndIsJoin(Long userId, Long studyId, Long senderId, boolean isJoin);

	void deleteBySenderIdAndStudyIdAndReceiverIdAndStateAndIsJoin(Long userId, Long studyId, Long receiverId, short state,boolean isJoin);

	List<StudyApply> findByStudyIdAndIsJoin(Long studyId, boolean isJoin);



//	@Query("SELECT * FROM STUDY_APPLY WHERE IS_JOIN = 1 AND SENDER_ID = ?1",nativeQuery = true)
	List<StudyApply> findBySender_Id(Long sender_id);

	StudyApply findBySender_IdAndStudy_IdAndReceiver_Id(Long userid, Long study_id, Long receiver_id);

	List<StudyApply> findByReceiver_Id(Long userid);

	StudyApply findByReceiver_IdAndStudy_IdAndSender_Id(Long userid, Long study_id, Long user_id);

	List<StudyApply> findByStudy_Id(Long studyId);

	Optional<StudyApply> findByStudyIdAndIsJoinAndReceiverId(Long studyId, boolean isJoin, Long receiverId);

	StudyApply findBySender_IdAndStudy_Id(Long userId, Long studyId);

}
