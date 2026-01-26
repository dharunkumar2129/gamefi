package com.practivce.auth.repository;
import  org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.practivce.auth.entity.Problems;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problems, Long> {
    
    // Custom Query: Find all problems by difficulty (e.g., fetch all "Easy" questions)
    // Usage: problemRepository.findByDifficulty(Difficulty.EASY);
	List<Problems> findByDifficulty(com.practivce.auth.entity.Difficulty difficulty);
}