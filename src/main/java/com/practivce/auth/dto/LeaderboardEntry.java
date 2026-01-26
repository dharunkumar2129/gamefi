package com.practivce.auth.dto;

import java.time.LocalDateTime; // <--- IMPORT THIS

public interface LeaderboardEntry {
    String getName();
    int getProblemsSolved();
    int getTotalScore();
    
    // CHANGE THIS from String to LocalDateTime
    LocalDateTime getLastUpdated(); 
}