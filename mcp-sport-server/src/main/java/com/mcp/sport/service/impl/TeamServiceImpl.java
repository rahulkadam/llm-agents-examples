package com.mcp.sport.service.impl;

import com.mcp.sport.domain.Team;
import com.mcp.sport.repository.TeamRepository;
import com.mcp.sport.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of TeamService
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    @Transactional(readOnly = true)
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Team getTeamByName(String name) {
        return teamRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with name: " + name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    @Transactional
    public Team updateTeam(Long id, Team team) {
        Team existingTeam = getTeamById(id);
        existingTeam.setName(team.getName());
        existingTeam.setCity(team.getCity());
        existingTeam.setHomeGround(team.getHomeGround());
        existingTeam.setLogoUrl(team.getLogoUrl());
        existingTeam.setTeamColor(team.getTeamColor());
        return teamRepository.save(existingTeam);
    }

    @Override
    @Transactional
    public void deleteTeam(Long id) {
        Team team = getTeamById(id);
        teamRepository.delete(team);
    }
} 