package com.tradingplatformByTrove.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Holding details of a specific asset item inside a Portfolio.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioAsset {
    private String assetSymbol;
    private String assetName;
    private BigDecimal quantity;
    private BigDecimal averageBuyPrice;
}
