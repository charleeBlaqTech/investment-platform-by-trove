package com.tradingplatformByTrove.gamification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Domain entity tracking user continuous trading streak counts.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreakTracker {
    private UUID userId;

    @Builder.Default
    private Integer currentStreak = 0;
}