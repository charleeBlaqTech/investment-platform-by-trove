package com.tradingplatformByTrove.portfolio.controller;

import com.tradingplatformByTrove.common.dto.ApiResponse;
import com.tradingplatformByTrove.portfolio.dto.AddAssetRequest;
import com.tradingplatformByTrove.portfolio.dto.PortfolioResponse;
import com.tradingplatformByTrove.portfolio.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PortfolioResponse>> getPortfolio(@PathVariable UUID userId) {
        PortfolioResponse portfolio = portfolioService.getPortfolioByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Portfolio retrieved successfully", portfolio));
    }

    @PostMapping("/user/{userId}/assets")
    public ResponseEntity<ApiResponse<PortfolioResponse>> addAsset(
            @PathVariable UUID userId,
            @Valid @RequestBody AddAssetRequest request) {
        PortfolioResponse portfolio = portfolioService.addAssetToPortfolio(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Asset added to portfolio", portfolio));
    }

    @DeleteMapping("/user/{userId}/assets/{assetSymbol}")
    public ResponseEntity<ApiResponse<PortfolioResponse>> removeAsset(
            @PathVariable UUID userId,
            @PathVariable String assetSymbol) {
        PortfolioResponse portfolio = portfolioService.removeAssetFromPortfolio(userId, assetSymbol);
        return ResponseEntity.ok(ApiResponse.success("Asset removed from portfolio", portfolio));
    }
}