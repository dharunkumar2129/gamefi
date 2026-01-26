package com.practivce.auth.entity;

import com.practivce.auth.entity.User; // Connecting to your Auth system
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many Submissions belong to One User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many Submissions belong to One Problem
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problems problem;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String codeSubmitted; // We store the code they wrote!

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status; // PASSED or FAILED

    @CreationTimestamp
    private LocalDateTime submittedAt; // Automatically sets the time

    // --- GETTERS AND SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Problems getProblem() {
        return problem;
    }

    public void setProblem(Problems problem) {
        this.problem = problem;
    }

    public String getCodeSubmitted() {
        return codeSubmitted;
    }

    public void setCodeSubmitted(String codeSubmitted) {
        this.codeSubmitted = codeSubmitted;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}