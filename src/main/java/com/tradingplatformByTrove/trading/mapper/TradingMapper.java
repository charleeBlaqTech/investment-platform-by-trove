package com.tradingplatformByTrove.trading.mapper;

import com.tradingplatformByTrove.portfolio.model.PortfolioAsset;
import com.tradingplatformByTrove.trading.dto.TradeRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapping utilities for trade conversions.
 */
@Component
public class TradingMapper {

    public PortfolioAsset toPortfolioAsset(
            TradeRequest request,
            BigDecimal currentPrice
    ) {
        return PortfolioAsset.builder()
                .assetSymbol(request.getAssetSymbol().toUpperCase())
                .assetName(request.getAssetSymbol().toUpperCase() + " Asset")
                .quantity(request.getQuantity())
                .averageBuyPrice(currentPrice)
                .build();
    }
}