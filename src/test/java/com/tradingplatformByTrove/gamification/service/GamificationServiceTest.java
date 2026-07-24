package com.tradingplatformByTrove.gamification.service;

import com.tradingplatformByTrove.gamification.mapper.GamificationMapper;
import com.tradingplatformByTrove.gamification.model.StreakTracker;
import com.tradingplatformByTrove.gamification.repository.GamificationRepository;
import com.tradingplatformByTrove.user.model.User;
import com.tradingplatformByTrove.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GamificationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GamificationRepository gamificationRepository;

    @Mock
    private GamificationMapper gamificationMapper;

    @InjectMocks
    private GamificationService gamificationService;

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = User.builder()
                .userId(userId)
                .username("trader_joe")
                .gemCount(0)
                .totalTradesExecuted(0)
                .rank(null)
                .build();
    }

    @Test
    @DisplayName("Trade 1: Base Gem + Streak 1 (Total 1 Gem)")
    void processTrade_FirstTrade_AwardsOneGem() {
        when(gamificationRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(new ArrayList<>(List.of(testUser)));

        gamificationService.processTradeGamification(testUser);

        assertEquals(1, testUser.getTotalTradesExecuted());
        assertEquals(1, testUser.getGemCount());
        verify(userRepository, times(2)).save(testUser);
    }

    @Test
    @DisplayName("Trade 3: Base Gem + 3-Streak Bonus (+3 Gems) = +4 Gems")
    void processTrade_ThirdTrade_AwardsStreakBonus() {
        testUser.setTotalTradesExecuted(2);
        testUser.setGemCount(2);

        StreakTracker streak = StreakTracker.builder().userId(userId).currentStreak(2).build();
        when(gamificationRepository.findByUserId(userId)).thenReturn(Optional.of(streak));
        when(userRepository.findAll()).thenReturn(new ArrayList<>(List.of(testUser)));

        gamificationService.processTradeGamification(testUser);

        assertEquals(3, testUser.getTotalTradesExecuted());
        assertEquals(6, testUser.getGemCount());
        assertEquals(3, streak.getCurrentStreak());
    }

    @Test
    @DisplayName("Trade 5: Base Gem + Milestone 5 Bonus (+5 Gems) = +6 Gems")
    void processTrade_FifthTrade_AwardsMilestoneBonus() {
        testUser.setTotalTradesExecuted(4);
        testUser.setGemCount(4);

        StreakTracker streak = StreakTracker.builder().userId(userId).currentStreak(4).build();
        when(gamificationRepository.findByUserId(userId)).thenReturn(Optional.of(streak));
        when(userRepository.findAll()).thenReturn(new ArrayList<>(List.of(testUser)));

        gamificationService.processTradeGamification(testUser);

        assertEquals(5, testUser.getTotalTradesExecuted());
        assertEquals(10, testUser.getGemCount());
    }

    @Test
    @DisplayName("Leaderboard Ranking: Standard competition ranking handles ties (1, 2, 2, 4)")
    void recalculateRanks_HandlesTiesCorrectly() {
        User u1 = User.builder().userId(UUID.randomUUID()).username("alpha").gemCount(100).build();
        User u2 = User.builder().userId(UUID.randomUUID()).username("beta").gemCount(80).build();
        User u3 = User.builder().userId(UUID.randomUUID()).username("gamma").gemCount(80).build();
        User u4 = User.builder().userId(UUID.randomUUID()).username("delta").gemCount(50).build();

        when(userRepository.findAll()).thenReturn(new ArrayList<>(List.of(u1, u2, u3, u4)));

        gamificationService.recalculateRanks();

        assertEquals(1, u1.getRank());
        assertEquals(2, u2.getRank());
        assertEquals(2, u3.getRank());
        assertEquals(4, u4.getRank());
    }
}