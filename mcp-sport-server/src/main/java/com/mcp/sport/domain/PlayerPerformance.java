package com.mcp.sport.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a player's performance in a match
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Entity
@Table(name = "player_performances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class PlayerPerformance extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    private Integer runsScored;
    private Integer ballsFaced;
    private Integer fours;
    private Integer sixes;
    private Double strikeRate;

    private Integer wicketsTaken;
    private Integer runsConceded;
    private Double oversBowled;
    private Integer maidens;
    private Double economyRate;

    private Integer catches;
    private Integer runOuts;
    private Integer stumpings;

    private Boolean playerOfMatch;
} 