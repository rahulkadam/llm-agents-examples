package com.mcp.sport.service;

import com.mcp.sport.domain.Team;
import java.util.List;

/**
 * Service interface for Team operations
 *
 * @author MCP Team
 * @since 1.0.0
 */
public interface TeamService {
    Team createTeam(Team team);
    Team getTeamById(Long id);
    Team getTeamByName(String name);
    List<Team> getAllTeams();
    Team updateTeam(Long id, Team team);
    void deleteTeam(Long id);
} 