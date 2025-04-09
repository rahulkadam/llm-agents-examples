package com.mcp.sport.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an IPL team
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Team extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String homeGround;

    private String logoUrl;
    private String teamColor;
} 