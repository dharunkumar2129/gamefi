package com.practivce.auth.service;


import com.practivce.auth.dto.SubmissionRequest;

public interface GameService {
    // Process a user's code submission
    String processSubmission(SubmissionRequest request);
    
    // Check if user already solved a problem (for NPC logic)
    boolean checkProblemStatus(Long userId, Long problemId);
}