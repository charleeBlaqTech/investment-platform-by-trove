package com.tradingplatformByTrove.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User Portfolio aggregate entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
    private UUID portfolioId;
    private UUID userId;

    @Builder.Default
    private BigDecimal cashBalance = new BigDecimal("10000.00");

    @Builder.Default
    private Map<String, PortfolioAsset> holdings = new ConcurrentHashMap<>();
}