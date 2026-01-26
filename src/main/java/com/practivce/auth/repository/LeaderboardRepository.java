package com.practivce.auth.repository;

import com.practivce.auth.entity.Leaderboard;
import com.practivce.auth.dto.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {

    // FIX: 
    // 1. We use u.email instead of u.name (safer)
    // 2. We assume the table is named 'users'. If your table is 'user', change 'JOIN users' to 'JOIN user'
    @Query(value = "SELECT " +
                   "COALESCE(u.email, 'Unknown Player') as name, " +
                   "l.problems_solved as problemsSolved, " +
                   "l.total_score as totalScore, " +
                   "l.last_updated as lastUpdated " +
                   "FROM leaderboard l " +
                   "LEFT JOIN users u ON l.user_id = u.id " +
                   "ORDER BY l.total_score DESC", nativeQuery = true)
    List<LeaderboardEntry> findTopPlayers();
}