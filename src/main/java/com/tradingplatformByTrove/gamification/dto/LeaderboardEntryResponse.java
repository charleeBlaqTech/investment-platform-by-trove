package com.tradingplatformByTrove.gamification.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LeaderboardEntryResponse {
    private Integer rank;
    private UUID userId;
    private String username;
    private Integer gemCount;
}