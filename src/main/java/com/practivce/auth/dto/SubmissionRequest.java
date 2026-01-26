package com.practivce.auth.dto;

public class SubmissionRequest {

    private Long userId;
    private Long problemId;
    private String code;

    // 1. MUST HAVE: Default Constructor
    public SubmissionRequest() {
    }

    // 2. All-Args Constructor
    public SubmissionRequest(Long userId, Long problemId, String code) {
        this.userId = userId;
        this.problemId = problemId;
        this.code = code;
    }

    // 3. Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}