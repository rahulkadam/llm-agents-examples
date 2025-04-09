package com.mcp.sport.service.context;

import com.mcp.sport.domain.*;
import com.mcp.sport.dto.context.ContextRequest;
import com.mcp.sport.dto.context.ContextResponse;
import com.mcp.sport.repository.MatchRepository;
import com.mcp.sport.repository.PlayerPerformanceRepository;
import com.mcp.sport.repository.PlayerRepository;
import com.mcp.sport.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.ArrayList;

/**
 * Service for providing rich context about IPL for LLMs and AI agents
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ContextService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final PlayerPerformanceRepository playerPerformanceRepository;

    @Transactional(readOnly = true)
    public ContextResponse processContextRequest(ContextRequest request) {
        ContextResponse response = new ContextResponse();
        response.setContextType(request.getContextType());
        response.setContextId(request.getContextId());

        switch (request.getContextType()) {
            case "TEAM" -> processTeamContext(request, response);
            case "PLAYER" -> processPlayerContext(request, response);
            case "MATCH" -> processMatchContext(request, response);
            case "SEASON" -> processSeasonContext(request, response);
            case "SEARCH" -> processSearchContext(request, response);
            default -> throw new IllegalArgumentException("Invalid context type: " + request.getContextType());
        }

        // Add natural language summary
        response.setNaturalLanguageSummary(generateNaturalLanguageSummary(response));
        
        // Add suggested queries
        response.setSuggestedQueries(generateSuggestedQueries(response));
        
        // Add related contexts
        response.setRelatedContexts(generateRelatedContexts(response));

        return response;
    }

    private void processTeamContext(ContextRequest request, ContextResponse response) {
        Long teamId = Long.parseLong(request.getContextId());
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        List<Player> players = playerRepository.findByTeam(team);
        List<Match> recentMatches = matchRepository.findTop5ByTeam1OrTeam2OrderByMatchDateTimeDesc(team, team);

        response.setData(convertToMap(team, request.getFields()));
        response.setRelationships(Map.of(
                "players", convertToMapList(players, request.getFields()),
                "recentMatches", convertToMapList(recentMatches, request.getFields())
        ));
        response.setStatistics(Map.of(
                "totalPlayers", players.size(),
                "recentForm", getRecentForm(team, recentMatches)
        ));
    }

    private void processPlayerContext(ContextRequest request, ContextResponse response) {
        Long playerId = Long.parseLong(request.getContextId());
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        List<PlayerPerformance> performances = playerPerformanceRepository.findByPlayer(player);
        List<Match> recentMatches = matchRepository.findTop5ByPlayerPerformances_PlayerOrderByMatchDateTimeDesc(player);

        response.setData(convertToMap(player, request.getFields()));
        response.setRelationships(Map.of(
                "team", convertToMap(player.getTeam(), request.getFields()),
                "recentPerformances", convertToMapList(performances.stream().limit(5).collect(Collectors.toList()), request.getFields()),
                "recentMatches", convertToMapList(recentMatches, request.getFields())
        ));
        response.setStatistics(Map.of(
                "totalMatches", performances.size(),
                "averageRuns", calculateAverageRuns(performances),
                "averageWickets", calculateAverageWickets(performances),
                "recentForm", getPlayerRecentForm(performances)
        ));
    }

    private void processMatchContext(ContextRequest request, ContextResponse response) {
        Long matchId = Long.parseLong(request.getContextId());
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        List<PlayerPerformance> performances = playerPerformanceRepository.findByMatch(match);

        response.setData(convertToMap(match, request.getFields()));
        response.setRelationships(Map.of(
                "team1", convertToMap(match.getTeam1(), request.getFields()),
                "team2", convertToMap(match.getTeam2(), request.getFields()),
                "performances", convertToMapList(performances, request.getFields())
        ));
        response.setStatistics(Map.of(
                "totalRuns", calculateTotalRuns(performances),
                "totalWickets", calculateTotalWickets(performances),
                "matchImpact", calculateMatchImpact(performances)
        ));
    }

    private void processSeasonContext(ContextRequest request, ContextResponse response) {
        String season = request.getContextId();
        List<Match> matches = matchRepository.findBySeason(season);
        List<Team> teams = teamRepository.findAll();
        List<PlayerPerformance> performances = playerPerformanceRepository.findByMatch_Season(season);

        response.setData(Map.of("season", season));
        response.setRelationships(Map.of(
                "teams", convertToMapList(teams, request.getFields()),
                "matches", convertToMapList(matches, request.getFields())
        ));
        response.setStatistics(Map.of(
                "totalMatches", matches.size(),
                "topScorers", getTopScorers(performances),
                "topWicketTakers", getTopWicketTakers(performances),
                "teamStandings", calculateTeamStandings(matches, teams)
        ));
    }

    private void processSearchContext(ContextRequest request, ContextResponse response) {
        String query = request.getContextId();
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        final String searchQuery = query.toLowerCase().trim();

        int limit = request.getParameters() != null && request.getParameters().containsKey("limit") ? 
            ((Number) request.getParameters().get("limit")).intValue() : 5;

        // Search across teams
        List<Team> teams = teamRepository.findAll().stream()
                .filter(team -> 
                    team.getName().toLowerCase().contains(searchQuery) ||
                    team.getCity().toLowerCase().contains(searchQuery) ||
                    team.getHomeGround().toLowerCase().contains(searchQuery))
                .limit(limit)
                .collect(Collectors.toList());

        // Search across players
        List<Player> players = playerRepository.findAll().stream()
                .filter(player -> 
                    player.getName().toLowerCase().contains(searchQuery) ||
                    (player.getNationality() != null && player.getNationality().toLowerCase().contains(searchQuery)) ||
                    player.getRole().toString().toLowerCase().contains(searchQuery))
                .limit(limit)
                .collect(Collectors.toList());

        // Search across matches
        List<Match> matches = matchRepository.findAll().stream()
                .filter(match -> {
                    String matchNumber = match.getMatchNumber();
                    String venue = match.getVenue();
                    String season = match.getSeason();
                    Team team1 = match.getTeam1();
                    Team team2 = match.getTeam2();
                    
                    return (matchNumber != null && matchNumber.toLowerCase().contains(searchQuery)) ||
                           (venue != null && venue.toLowerCase().contains(searchQuery)) ||
                           (season != null && season.toLowerCase().contains(searchQuery)) ||
                           (team1 != null && team1.getName().toLowerCase().contains(searchQuery)) ||
                           (team2 != null && team2.getName().toLowerCase().contains(searchQuery));
                })
                .limit(limit)
                .collect(Collectors.toList());

        response.setData(Map.of(
            "query", searchQuery,
            "limit", limit
        ));
        
        response.setRelationships(Map.of(
            "teams", convertToMapList(teams, null),
            "players", convertToMapList(players, null),
            "matches", convertToMapList(matches, null)
        ));
        
        response.setStatistics(Map.of(
            "totalTeamsFound", teams.size(),
            "totalPlayersFound", players.size(),
            "totalMatchesFound", matches.size()
        ));

        // Generate natural language summary
        String summary = String.format(
            "Found %d teams, %d players, and %d matches matching '%s'",
            teams.size(), players.size(), matches.size(), searchQuery
        );
        response.setNaturalLanguageSummary(summary);

        // Generate suggested queries
        List<String> suggestions = new ArrayList<>();
        if (!teams.isEmpty()) {
            suggestions.add("Get details for team " + teams.get(0).getName());
        }
        if (!players.isEmpty()) {
            suggestions.add("Show performance of player " + players.get(0).getName());
        }
        if (!matches.isEmpty()) {
            suggestions.add("Show match details for " + matches.get(0).getMatchNumber());
        }
        response.setSuggestedQueries(suggestions.toArray(new String[0]));
    }

    private Map<String, Object> convertToMap(Object object, String[] fields) {
        if (object == null) {
            return Map.of();
        }

        // Default fields based on object type
        String[] fieldsToUse = fields;
        if (fieldsToUse == null || fieldsToUse.length == 0) {
            if (object instanceof Team) {
                fieldsToUse = new String[]{"id", "name", "city", "homeGround"};
            } else if (object instanceof Player) {
                fieldsToUse = new String[]{"id", "name", "role", "battingStyle", "bowlingStyle"};
            } else if (object instanceof Match) {
                fieldsToUse = new String[]{"id", "matchDateTime", "venue", "season", "status"};
            } else if (object instanceof PlayerPerformance) {
                fieldsToUse = new String[]{"id", "runsScored", "wicketsTaken", "ballsFaced", "oversBowled"};
            } else {
                return Map.of("id", object.toString());
            }
        }

        Map<String, Object> result = new HashMap<>();
        try {
            for (String field : fieldsToUse) {
                try {
                    Method getter = object.getClass().getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1));
                    Object value = getter.invoke(object);
                    result.put(field, value);
                } catch (NoSuchMethodException e) {
                    // Skip fields that don't exist
                    continue;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to map: " + e.getMessage(), e);
        }
        return result;
    }

    private List<Map<String, Object>> convertToMapList(List<?> objects, String[] fields) {
        if (objects == null || objects.isEmpty()) {
            return List.of();
        }

        return objects.stream()
                .map(obj -> convertToMap(obj, fields))
                .collect(Collectors.toList());
    }

    private String generateNaturalLanguageSummary(ContextResponse response) {
        // Implementation for generating natural language summary
        return "";
    }

    private String[] generateSuggestedQueries(ContextResponse response) {
        // Implementation for generating suggested queries
        return new String[0];
    }

    private String[] generateRelatedContexts(ContextResponse response) {
        // Implementation for generating related contexts
        return new String[0];
    }

    private Map<String, Object> getTeamContext(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        List<Player> players = playerRepository.findByTeam(team);
        List<Match> recentMatches = matchRepository.findTop5ByTeam1OrTeam2OrderByMatchDateTimeDesc(team, team);

        return Map.of(
                "team", team,
                "players", players,
                "recentMatches", recentMatches,
                "statistics", Map.of(
                        "totalPlayers", players.size(),
                        "recentForm", getRecentForm(team, recentMatches)
                )
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPlayerContext(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        List<PlayerPerformance> performances = playerPerformanceRepository.findByPlayer(player);
        List<Match> recentMatches = matchRepository.findTop5ByPlayerPerformances_PlayerOrderByMatchDateTimeDesc(player);

        return Map.of(
                "player", player,
                "recentPerformances", performances.stream()
                        .limit(5)
                        .collect(Collectors.toList()),
                "statistics", Map.of(
                        "totalMatches", performances.size(),
                        "averageRuns", calculateAverageRuns(performances),
                        "averageWickets", calculateAverageWickets(performances),
                        "recentForm", getPlayerRecentForm(performances)
                )
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMatchContext(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        List<PlayerPerformance> performances = playerPerformanceRepository.findByMatch(match);

        return Map.of(
                "match", match,
                "performances", performances,
                "keyMoments", extractKeyMoments(performances),
                "statistics", Map.of(
                        "totalRuns", calculateTotalRuns(performances),
                        "totalWickets", calculateTotalWickets(performances),
                        "matchImpact", calculateMatchImpact(performances)
                )
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getSeasonContext(String season) {
        List<Match> matches = matchRepository.findBySeason(season);
        List<Team> teams = teamRepository.findAll();
        List<PlayerPerformance> performances = playerPerformanceRepository.findByMatch_Season(season);

        return Map.of(
                "season", season,
                "teams", teams,
                "matches", matches,
                "statistics", Map.of(
                        "totalMatches", matches.size(),
                        "topScorers", getTopScorers(performances),
                        "topWicketTakers", getTopWicketTakers(performances),
                        "teamStandings", calculateTeamStandings(matches, teams)
                )
        );
    }

    private Map<String, Object> getRecentForm(Team team, List<Match> recentMatches) {
        if (recentMatches == null || recentMatches.isEmpty()) {
            return Map.of(
                "matchesPlayed", 0,
                "wins", 0,
                "losses", 0,
                "winPercentage", 0.0,
                "form", new ArrayList<String>()
            );
        }

        int wins = 0;
        int losses = 0;
        List<String> form = new ArrayList<>();

        for (Match match : recentMatches) {
            if (match.getStatus() != Match.MatchStatus.COMPLETED) {
                form.add("P"); // Pending
                continue;
            }

            if (match.getMatchWinner() != null) {
                if (match.getMatchWinner().equals(team)) {
                    wins++;
                    form.add("W");
                } else {
                    losses++;
                    form.add("L");
                }
            } else {
                form.add("NR"); // No Result
            }
        }

        int matchesPlayed = wins + losses;
        double winPercentage = matchesPlayed > 0 ? (double) wins / matchesPlayed * 100 : 0.0;

        Map<String, Object> result = new HashMap<>();
        result.put("matchesPlayed", matchesPlayed);
        result.put("wins", wins);
        result.put("losses", losses);
        result.put("winPercentage", winPercentage);
        result.put("form", form);
        return result;
    }

    private double calculateAverageRuns(List<PlayerPerformance> performances) {
        if (performances.isEmpty()) {
            return 0.0;
        }
        return performances.stream()
                .mapToInt(PlayerPerformance::getRunsScored)
                .average()
                .orElse(0.0);
    }

    private double calculateAverageWickets(List<PlayerPerformance> performances) {
        if (performances.isEmpty()) {
            return 0.0;
        }
        return performances.stream()
                .mapToInt(PlayerPerformance::getWicketsTaken)
                .average()
                .orElse(0.0);
    }

    private Map<String, Object> getPlayerRecentForm(List<PlayerPerformance> performances) {
        List<PlayerPerformance> recentPerformances = performances.stream()
                .limit(5)
                .collect(Collectors.toList());

        return Map.of(
                "matches", recentPerformances.size(),
                "totalRuns", recentPerformances.stream().mapToInt(PlayerPerformance::getRunsScored).sum(),
                "totalWickets", recentPerformances.stream().mapToInt(PlayerPerformance::getWicketsTaken).sum(),
                "averageRuns", calculateAverageRuns(recentPerformances),
                "averageWickets", calculateAverageWickets(recentPerformances)
        );
    }

    private List<Map<String, Object>> extractKeyMoments(List<PlayerPerformance> performances) {
        // Implementation for extracting key moments from match
        return List.of();
    }

    private int calculateTotalRuns(List<PlayerPerformance> performances) {
        return performances.stream()
                .mapToInt(PlayerPerformance::getRunsScored)
                .sum();
    }

    private int calculateTotalWickets(List<PlayerPerformance> performances) {
        return performances.stream()
                .mapToInt(PlayerPerformance::getWicketsTaken)
                .sum();
    }

    private Map<String, Object> calculateMatchImpact(List<PlayerPerformance> performances) {
        return Map.of(
                "totalRuns", calculateTotalRuns(performances),
                "totalWickets", calculateTotalWickets(performances),
                "keyPerformances", performances.stream()
                        .filter(p -> p.getRunsScored() > 50 || p.getWicketsTaken() > 2)
                        .map(p -> Map.of(
                                "player", p.getPlayer().getName(),
                                "runs", p.getRunsScored(),
                                "wickets", p.getWicketsTaken()
                        ))
                        .collect(Collectors.toList())
        );
    }

    private List<Map<String, Object>> getTopScorers(List<PlayerPerformance> performances) {
        Map<Player, Integer> playerRuns = performances.stream()
                .collect(Collectors.groupingBy(
                        PlayerPerformance::getPlayer,
                        Collectors.summingInt(PlayerPerformance::getRunsScored)
                ));

        return playerRuns.entrySet().stream()
                .sorted(Map.Entry.<Player, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("player", entry.getKey().getName());
                    result.put("totalRuns", entry.getValue());
                    return result;
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> getTopWicketTakers(List<PlayerPerformance> performances) {
        Map<Player, Integer> playerWickets = performances.stream()
                .collect(Collectors.groupingBy(
                        PlayerPerformance::getPlayer,
                        Collectors.summingInt(PlayerPerformance::getWicketsTaken)
                ));

        return playerWickets.entrySet().stream()
                .sorted(Map.Entry.<Player, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("player", entry.getKey().getName());
                    result.put("totalWickets", entry.getValue());
                    return result;
                })
                .collect(Collectors.toList());
    }

    private Map<String, Object> calculateTeamStandings(List<Match> matches, List<Team> teams) {
        Map<Team, Integer> points = new HashMap<>();
        Map<Team, Integer> matchesPlayed = new HashMap<>();
        Map<Team, Integer> matchesWon = new HashMap<>();

        // Initialize all teams with 0 points
        teams.forEach(team -> {
            points.put(team, 0);
            matchesPlayed.put(team, 0);
            matchesWon.put(team, 0);
        });

        // Calculate points for each team
        matches.forEach(match -> {
            if (match.getStatus() == Match.MatchStatus.COMPLETED && match.getMatchWinner() != null) {
                Team winner = match.getMatchWinner();
                points.put(winner, points.get(winner) + 2);
                matchesWon.put(winner, matchesWon.get(winner) + 1);
            }
            matchesPlayed.put(match.getTeam1(), matchesPlayed.get(match.getTeam1()) + 1);
            matchesPlayed.put(match.getTeam2(), matchesPlayed.get(match.getTeam2()) + 1);
        });

        // Convert to list of standings with proper type handling
        List<Map<String, Object>> standings = teams.stream()
                .map(team -> {
                    Map<String, Object> teamStanding = new HashMap<>();
                    teamStanding.put("team", team.getName());
                    teamStanding.put("points", points.get(team));
                    teamStanding.put("matchesPlayed", matchesPlayed.get(team));
                    teamStanding.put("matchesWon", matchesWon.get(team));
                    return teamStanding;
                })
                .sorted((a, b) -> Integer.compare((Integer) b.get("points"), (Integer) a.get("points")))
                .collect(Collectors.toList());

        return Map.of("standings", standings);
    }
} 