package com.tradingplatformByTrove.gamification.service;

import com.tradingplatformByTrove.gamification.dto.LeaderboardEntryResponse;
import com.tradingplatformByTrove.gamification.mapper.GamificationMapper;
import com.tradingplatformByTrove.gamification.model.StreakTracker;
import com.tradingplatformByTrove.gamification.repository.GamificationRepository;
import com.tradingplatformByTrove.user.model.User;
import com.tradingplatformByTrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GamificationService {

    private final UserRepository userRepository;
    private final GamificationRepository gamificationRepository;
    private final GamificationMapper gamificationMapper;

    /**
     * Calculates Gem allocations (1 base gem + milestone bonuses + streak bonuses).
     */
    public synchronized void processTradeGamification(User user) {
        int updatedTradeCount = user.getTotalTradesExecuted() + 1;
        user.setTotalTradesExecuted(updatedTradeCount);

        int gemsAwarded = 1; // Base rule: 1 gem per trade

        // Milestone calculation
        if (updatedTradeCount == 5) {
            gemsAwarded += 5;
        } else if (updatedTradeCount == 10) {
            gemsAwarded += 10;
        }

        // Streak calculation bonus (Bonus: +3 gems every 3 consecutive trades)
        StreakTracker streakTracker = gamificationRepository.findByUserId(user.getUserId())
                .orElse(StreakTracker.builder().userId(user.getUserId()).currentStreak(0).build());

        int newStreak = streakTracker.getCurrentStreak() + 1;
        streakTracker.setCurrentStreak(newStreak);
        if (newStreak % 3 == 0) {
            gemsAwarded += 3;
        }
        gamificationRepository.save(streakTracker);

        user.setGemCount(user.getGemCount() + gemsAwarded);
        userRepository.save(user);

        // Synchronize leaderboards dynamically
        recalculateRanks();
    }

    /**
     * Updates ranks in descending order of gem counts, handling ties (1, 2, 2, 4 pattern).
     */
    public synchronized void recalculateRanks() {
        // Wrap in ArrayList to guarantee the list is mutable, even in Mockito unit tests
        List<User> users = new java.util.ArrayList<>(userRepository.findAll());

        if (users.isEmpty()) {
            return;
        }

        users.sort(Comparator.comparing(User::getGemCount).reversed());

        int currentRank = 1;
        for (int i = 0; i < users.size(); i++) {
            if (i > 0 && !users.get(i).getGemCount().equals(users.get(i - 1).getGemCount())) {
                currentRank = i + 1;
            }
            users.get(i).setRank(currentRank);
            userRepository.save(users.get(i));
        }
    }

    /**
     * Gets top N leaderboard users.
     */
    public List<LeaderboardEntryResponse> getLeaderboard(int limit) {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getRank, Comparator.nullsLast(Comparator.naturalOrder())))
                .limit(limit)
                .map(gamificationMapper::toLeaderboardResponse)
                .collect(Collectors.toList());
    }
}