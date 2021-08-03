package com.ppakgom.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ppakgom.db.entity.UserConference;

@Repository
public interface UserConferenceRepository extends JpaRepository<UserConference, Long> { // <엔티티, 식별자>
    @Transactional
    void deleteAllByUserId(Long userId);

	Optional<UserConference[]> findByConference_Id(Long conference_id);
}
