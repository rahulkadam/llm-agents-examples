package com.mcp.sport.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an IPL match
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime matchDateTime;

    @Column(nullable = false)
    private String venue;

    private String season;
    private String matchNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team1_id", nullable = false)
    private Team team1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team2_id", nullable = false)
    private Team team2;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    private String result;

    @ManyToOne
    @JoinColumn(name = "toss_winner_id")
    private Team tossWinner;

    @Enumerated(EnumType.STRING)
    private TossDecision tossDecision;

    private Integer team1Score;
    private Integer team1Wickets;
    private Float team1Overs;

    private Integer team2Score;
    private Integer team2Wickets;
    private Float team2Overs;

    @ManyToOne
    @JoinColumn(name = "match_winner_id")
    private Team matchWinner;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerPerformance> playerPerformances;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum MatchStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        ABANDONED,
        CANCELLED
    }

    public enum TossDecision {
        BAT,
        BOWL
    }
} 