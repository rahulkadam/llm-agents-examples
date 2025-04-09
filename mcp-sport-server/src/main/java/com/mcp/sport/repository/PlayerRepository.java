package com.mcp.sport.repository;

import com.mcp.sport.domain.Player;
import com.mcp.sport.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Player entity
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByTeam(Team team);
} 