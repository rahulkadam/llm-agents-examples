package com.mcp.sport.repository;

import com.mcp.sport.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Team entity
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);
} 