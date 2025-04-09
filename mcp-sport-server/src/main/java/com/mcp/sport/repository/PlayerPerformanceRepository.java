package com.mcp.sport.repository;

import com.mcp.sport.domain.Match;
import com.mcp.sport.domain.Player;
import com.mcp.sport.domain.PlayerPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for PlayerPerformance entity
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Repository
public interface PlayerPerformanceRepository extends JpaRepository<PlayerPerformance, Long> {
    List<PlayerPerformance> findByPlayer(Player player);
    List<PlayerPerformance> findByMatch(Match match);
    List<PlayerPerformance> findByMatch_Season(String season);
} 