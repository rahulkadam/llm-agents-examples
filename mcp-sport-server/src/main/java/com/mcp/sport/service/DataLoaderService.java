package com.mcp.sport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.sport.domain.*;
import com.mcp.sport.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataLoaderService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final PlayerPerformanceRepository playerPerformanceRepository;
    private final ObjectMapper objectMapper;

    public void loadData() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/ipl_data.json");
        Map<String, Object> data = objectMapper.readValue(resource.getInputStream(), Map.class);

        // Load teams
        Map<Long, Team> teamMap = new HashMap<>();
        for (Map<String, Object> teamData : (Iterable<Map<String, Object>>) data.get("teams")) {
            Team team = Team.builder()
                    .name((String) teamData.get("name"))
                    .city((String) teamData.get("city"))
                    .homeGround((String) teamData.get("homeGround"))
                    .teamColor((String) teamData.get("teamColor"))
                    .logoUrl((String) teamData.get("logoUrl"))
                    .build();
            team = teamRepository.save(team);
            teamMap.put(((Number) teamData.get("id")).longValue(), team);
        }

        // Load players
        Map<Long, Player> playerMap = new HashMap<>();
        for (Map<String, Object> playerData : (Iterable<Map<String, Object>>) data.get("players")) {
            Player player = Player.builder()
                    .name((String) playerData.get("name"))
                    .dateOfBirth(LocalDate.parse((String) playerData.get("dateOfBirth")))
                    .role(Player.PlayerRole.valueOf((String) playerData.get("role")))
                    .battingStyle(Player.BattingStyle.valueOf((String) playerData.get("battingStyle")))
                    .bowlingStyle(Player.BowlingStyle.valueOf((String) playerData.get("bowlingStyle")))
                    .team(teamMap.get(((Number) playerData.get("teamId")).longValue()))
                    .jerseyNumber(((Number) playerData.get("jerseyNumber")).intValue())
                    .nationality((String) playerData.get("nationality"))
                    .photoUrl((String) playerData.get("photoUrl"))
                    .build();
            player = playerRepository.save(player);
            playerMap.put(((Number) playerData.get("id")).longValue(), player);
        }

        // Load matches
        Map<Long, Match> matchMap = new HashMap<>();
        for (Map<String, Object> matchData : (Iterable<Map<String, Object>>) data.get("matches")) {
            Match match = Match.builder()
                    .matchDateTime(LocalDateTime.parse((String) matchData.get("matchDateTime")))
                    .venue((String) matchData.get("venue"))
                    .season((String) matchData.get("season"))
                    .matchNumber((String) matchData.get("matchNumber"))
                    .team1(teamMap.get(((Number) matchData.get("team1Id")).longValue()))
                    .team2(teamMap.get(((Number) matchData.get("team2Id")).longValue()))
                    .status(Match.MatchStatus.valueOf((String) matchData.get("status")))
                    .team1Score(matchData.get("team1Score") != null ? ((Number) matchData.get("team1Score")).intValue() : null)
                    .team1Wickets(matchData.get("team1Wickets") != null ? ((Number) matchData.get("team1Wickets")).intValue() : null)
                    .team1Overs(matchData.get("team1Overs") != null ? ((Number) matchData.get("team1Overs")).floatValue() : null)
                    .team2Score(matchData.get("team2Score") != null ? ((Number) matchData.get("team2Score")).intValue() : null)
                    .team2Wickets(matchData.get("team2Wickets") != null ? ((Number) matchData.get("team2Wickets")).intValue() : null)
                    .team2Overs(matchData.get("team2Overs") != null ? ((Number) matchData.get("team2Overs")).floatValue() : null)
                    .matchWinner(matchData.get("matchWinnerId") != null ? teamMap.get(((Number) matchData.get("matchWinnerId")).longValue()) : null)
                    .build();
            match = matchRepository.save(match);
            matchMap.put(((Number) matchData.get("id")).longValue(), match);
        }

        // Load player performances
        for (Map<String, Object> performanceData : (Iterable<Map<String, Object>>) data.get("playerPerformances")) {
            PlayerPerformance performance = PlayerPerformance.builder()
                    .player(playerMap.get(((Number) performanceData.get("playerId")).longValue()))
                    .match(matchMap.get(((Number) performanceData.get("matchId")).longValue()))
                    .runsScored(((Number) performanceData.get("runsScored")).intValue())
                    .ballsFaced(((Number) performanceData.get("ballsFaced")).intValue())
                    .fours(((Number) performanceData.get("fours")).intValue())
                    .sixes(((Number) performanceData.get("sixes")).intValue())
                    .strikeRate(((Number) performanceData.get("strikeRate")).doubleValue())
                    .wicketsTaken(((Number) performanceData.get("wicketsTaken")).intValue())
                    .runsConceded(((Number) performanceData.get("runsConceded")).intValue())
                    .oversBowled(((Number) performanceData.get("oversBowled")).doubleValue())
                    .maidens(((Number) performanceData.get("maidens")).intValue())
                    .economyRate(((Number) performanceData.get("economyRate")).doubleValue())
                    .catches(((Number) performanceData.get("catches")).intValue())
                    .runOuts(((Number) performanceData.get("runOuts")).intValue())
                    .stumpings(((Number) performanceData.get("stumpings")).intValue())
                    .playerOfMatch((Boolean) performanceData.get("playerOfMatch"))
                    .build();
            playerPerformanceRepository.save(performance);
        }
    }
} 