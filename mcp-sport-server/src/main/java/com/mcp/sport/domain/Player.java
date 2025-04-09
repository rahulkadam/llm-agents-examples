package com.mcp.sport.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents an IPL player
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Player extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BattingStyle battingStyle;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BowlingStyle bowlingStyle;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column
    private Integer jerseyNumber;

    @Column
    private String nationality;

    @Column
    private String photoUrl;

    @Column
    private String bio;

    @Column
    private Double basePrice;

    @Column
    private Double currentPrice;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum PlayerRole {
        BATSMAN,
        BOWLER,
        ALL_ROUNDER,
        WICKETKEEPER
    }

    public enum BattingStyle {
        RIGHT_HANDED,
        LEFT_HANDED
    }

    public enum BowlingStyle {
        RIGHT_ARM_FAST,
        RIGHT_ARM_MEDIUM,
        RIGHT_ARM_OFFBREAK,
        RIGHT_ARM_LEGBREAK,
        LEFT_ARM_FAST,
        LEFT_ARM_MEDIUM,
        LEFT_ARM_ORTHODOX,
        LEFT_ARM_UNORTHODOX
    }
} 