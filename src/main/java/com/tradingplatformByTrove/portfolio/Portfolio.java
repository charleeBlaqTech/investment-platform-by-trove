package com.tradingplatformByTrove.portfolio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Domain entity representing a user's asset holdings and available cash.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
    private UUID portfolioId;
    private UUID userId;

    @Builder.Default
    private BigDecimal cashBalance = new BigDecimal("10000.00"); // Initial virtual capital

    @Builder.Default
    private Map<String, PortfolioAsset> holdings = new ConcurrentHashMap<>();

    public BigDecimal calculateTotalValue(Map<String, BigDecimal> currentPrices) {
        BigDecimal assetValue = holdings.values().stream()
                .map(holding -> {
                    BigDecimal price = currentPrices.getOrDefault(holding.getAssetSymbol(), BigDecimal.ZERO);
                    return holding.calculateMarketValue(price);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return cashBalance.add(assetValue);
    }
}
