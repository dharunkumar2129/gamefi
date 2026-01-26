package com.practivce.auth.controller;

import com.practivce.auth.entity.Problems;
import com.practivce.auth.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@CrossOrigin(origins = "*") // Allows Unity/React to access this
public class ProblemController {

    @Autowired
    private ProblemRepository problemRepository;

    // 1. Get All Problems (For the Quest Log menu)
    // URL: GET http://localhost:8080/api/problems
    @GetMapping
    public List<Problems> getAllProblems() {
        return problemRepository.findAll();
    }

    // 2. Get Specific Problem (When talking to an NPC)
    // URL: GET http://localhost:8080/api/problems/5
    @GetMapping("/{id}")
    public ResponseEntity<Problems> getProblemById(@PathVariable Long id) {
        return problemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}