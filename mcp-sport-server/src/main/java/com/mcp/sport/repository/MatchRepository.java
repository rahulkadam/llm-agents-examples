package com.mcp.sport.repository;

import com.mcp.sport.domain.Match;
import com.mcp.sport.domain.Player;
import com.mcp.sport.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Match entity
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findTop5ByTeam1OrTeam2OrderByMatchDateTimeDesc(Team team1, Team team2);
    List<Match> findTop5ByPlayerPerformances_PlayerOrderByMatchDateTimeDesc(Player player);
    List<Match> findBySeason(String season);
} 