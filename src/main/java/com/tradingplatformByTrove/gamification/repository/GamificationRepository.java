package com.tradingplatformByTrove.gamification.repository;

import com.tradingplatformByTrove.gamification.model.StreakTracker;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Storage layer for Gamification streak metadata.
 */
@Repository
public class GamificationRepository {
    private final Map<UUID, StreakTracker> streakStorage = new ConcurrentHashMap<>();

    public StreakTracker save(StreakTracker tracker) {
        streakStorage.put(tracker.getUserId(), tracker);
        return tracker;
    }

    public Optional<StreakTracker> findByUserId(UUID userId) {
        return Optional.ofNullable(streakStorage.get(userId));
    }
}