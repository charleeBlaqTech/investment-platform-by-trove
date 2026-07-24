package com.tradingplatformByTrove.gamification.controller;

import com.tradingplatformByTrove.common.dto.ApiResponse;
import com.tradingplatformByTrove.gamification.dto.LeaderboardEntryResponse;
import com.tradingplatformByTrove.gamification.service.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gamification")
@RequiredArgsConstructor
public class GamificationController {

    private final GamificationService gamificationService;

    @GetMapping("/leaderboard")
    public ResponseEntity<ApiResponse<List<LeaderboardEntryResponse>>> getLeaderboard(
            @RequestParam(defaultValue = "10") int limit) {
        List<LeaderboardEntryResponse> leaderboard = gamificationService.getLeaderboard(limit);
        return ResponseEntity.ok(ApiResponse.success("Leaderboard retrieved successfully", leaderboard));
    }
}