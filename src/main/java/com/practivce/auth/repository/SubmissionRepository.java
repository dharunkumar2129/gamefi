package com.practivce.auth.repository;

import com.practivce.auth.entity.Submissions;
import com.practivce.auth.entity.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submissions, Long> {

    // 1. Check if a user has solved a specific problem
    // This returns TRUE if a record exists with the UserID, ProblemID, and Status='PASSED'
    boolean existsByUserIdAndProblemIdAndStatus(Long userId, Long problemId, SubmissionStatus status);

    // 2. Count how many problems a user has solved (Useful for profile stats)
    long countByUserIdAndStatus(Long userId, SubmissionStatus status);
}
