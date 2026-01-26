package com.practivce.auth.controller;

import com.practivce.auth.dto.SubmissionRequest;
import com.practivce.auth.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "*")
public class SubmissionController {

    @Autowired
    private GameService gameService;

    // 1. Submit Code (The "Run" Button)
    // URL: POST http://localhost:8080/api/submissions
    // Body: { "userId": 1, "problemId": 2, "code": "..." }
    @PostMapping
    public ResponseEntity<?> submitCode(@RequestBody SubmissionRequest request) {
        // 👇 ADD THIS LINE
        System.out.println("🔥 CONTROLLER HIT! Received code: " + request.getCode());

        try {
            String result = gameService.processSubmission(request);
            return ResponseEntity.ok(Collections.singletonMap("message", result));
        } catch (Exception e) {
            // 👇 AND PRINT THE ERROR HERE
            e.printStackTrace(); 
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // 2. Check Status (For NPC overhead icons)
    // URL: GET http://localhost:8080/api/submissions/status?userId=1&problemId=2
    @GetMapping("/status")
    public ResponseEntity<?> checkStatus(@RequestParam Long userId, @RequestParam Long problemId) {
        boolean isSolved = gameService.checkProblemStatus(userId, problemId);
        return ResponseEntity.ok(Collections.singletonMap("isSolved", isSolved));
    }
}