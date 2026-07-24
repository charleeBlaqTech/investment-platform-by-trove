package com.tradingplatformByTrove.portfolio.dto;

import com.tradingplatformByTrove.portfolio.model.PortfolioAsset;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class PortfolioResponse {
    private UUID portfolioId;
    private UUID userId;
    private BigDecimal cashBalance;
    private Map<String, PortfolioAsset> holdings;
    private BigDecimal totalPortfolioValue;
}