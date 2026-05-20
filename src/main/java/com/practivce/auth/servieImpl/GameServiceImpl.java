package com.practivce.auth.servieImpl;


import com.practivce.auth.*; // Import Problems, Submissions, Leaderboard, etc.
import com.practivce.auth.dto.SubmissionRequest;
import com.practivce.auth.entity.User;
import com.practivce.auth.entity.Leaderboard;
import com.practivce.auth.entity.Problems;
import com.practivce.auth.entity.SubmissionStatus;
import com.practivce.auth.entity.Submissions;
import com.practivce.auth.repository.*;
import com.practivce.auth.service.CompilerService;
import com.practivce.auth.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameServiceImpl implements GameService {

    @Autowired private SubmissionRepository submissionRepository;
    @Autowired private ProblemRepository problemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private LeaderboardRepository leaderboardRepository;
    @Autowired private CompilerService compilerService;

    @Override
    public boolean checkProblemStatus(Long userId, Long problemId) {
        return submissionRepository.existsByUserIdAndProblemIdAndStatus(
                userId, problemId, SubmissionStatus.PASSED);
    }

    @Override
    @Transactional
    public String processSubmission(SubmissionRequest request) {
        // 1. Fetch User and Problem
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Problems problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        // 2. Run Code against Hidden Test Case
        String actualOutput = compilerService.execute(
                request.getCode(),
                "java",
                problem.getTestCaseInput() // Database Input
        );

        // 3. Compare Output
        boolean passed = actualOutput.equals(problem.getExpectedOutput().trim());

        // Check if user already solved it BEFORE saving the new submission
        boolean alreadySolved = false;
        if (passed) {
            alreadySolved = submissionRepository.existsByUserIdAndProblemIdAndStatus(
                    user.getId(), problem.getId(), SubmissionStatus.PASSED);
        }

        // 4. Save Submission History
        Submissions submission = new Submissions();
        submission.setUser(user);
        submission.setProblem(problem);
        submission.setCodeSubmitted(request.getCode());
        submission.setStatus(passed ? SubmissionStatus.PASSED : SubmissionStatus.FAILED);
        submissionRepository.save(submission);

        if (!passed) {
            return "FAILED\nExpected: " + problem.getExpectedOutput() + "\nGot: " + actualOutput;
        }

        // 5. Update Leaderboard (Only if passed and not solved before)
        if (!alreadySolved) {
            Leaderboard lb = leaderboardRepository.findById(user.getId()).orElse(new Leaderboard());
            lb.setUser(user);
            lb.setTotalScore(lb.getTotalScore() + problem.getXpReward());
            lb.setProblemsSolved(lb.getProblemsSolved() + 1);
            leaderboardRepository.save(lb);

            // Also update User entity stats
            user.setXp(user.getXp() + problem.getXpReward());
            userRepository.save(user);
        }

        return "SUCCESS";
    }
}