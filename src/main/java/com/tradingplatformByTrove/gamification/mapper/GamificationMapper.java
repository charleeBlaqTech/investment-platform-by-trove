package com.tradingplatformByTrove.gamification.mapper;

import com.tradingplatformByTrove.gamification.dto.LeaderboardEntryResponse;
import com.tradingplatformByTrove.user.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper component for Leaderboard & Gamification mappings.
 */
@Component
public class GamificationMapper {

    public LeaderboardEntryResponse toLeaderboardResponse(User user) {
        return LeaderboardEntryResponse.builder()
                .rank(user.getRank())
                .userId(user.getUserId())
                .username(user.getUsername())
                .gemCount(user.getGemCount())
                .build();
    }
}