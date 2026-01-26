package com.practivce.auth.controller;

import com.practivce.auth.dto.LeaderboardEntry;
import com.practivce.auth.repository.LeaderboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@CrossOrigin(origins = "*")
public class LeaderboardController {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @GetMapping
    public List<LeaderboardEntry> getLeaderboard() {
        return leaderboardRepository.findTopPlayers();
    }
}