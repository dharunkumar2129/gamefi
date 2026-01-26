package com.practivce.auth.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "leaderboard")
public class Leaderboard {

    @Id
    @Column(name = "user_id")
    private Long userId;

    // Connects 1-to-1 with the User
    // When User is deleted, delete their Leaderboard entry too
    @OneToOne
    @MapsId 
    @JoinColumn(name = "user_id")
    private User user;

    private Long totalScore;       // e.g. 1500 XP
    private int problemsSolved;    // e.g. 12 problems

    @UpdateTimestamp // Auto-updates whenever score changes
    private LocalDateTime lastUpdated;

    // --- GETTERS AND SETTERS ---

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    public int getProblemsSolved() {
        return problemsSolved;
    }

    public void setProblemsSolved(int problemsSolved) {
        this.problemsSolved = problemsSolved;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}