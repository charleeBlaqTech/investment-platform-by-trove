package com.tradingplatformByTrove.portfolio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Individual asset holding item inside a Portfolio.
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

    public BigDecimal calculateMarketValue(BigDecimal currentPrice) {
        return currentPrice.multiply(quantity);
    }
}